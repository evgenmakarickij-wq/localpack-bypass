package com.localpack.bypass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Checks GitHub Releases for a newer build of this mod and installs it.
 *
 * <p>Nothing here runs on the client thread: {@link #check} and {@link #install} hand their work to
 * {@link #IO} and complete the returned future on that pool. The caller is expected to hop back to
 * the client thread with {@code Minecraft.getInstance().execute(...)}.
 *
 * <p>Installing means: download the jar, verify its SHA-256 against the manifest published in the
 * same release, put it into {@code mods/} and remove the jar the game is currently running from.
 * That last step cannot always happen in-process (Windows keeps the running jar locked), see
 * {@link #removeRunningJar}.
 */
public final class Updater {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    public static final String MOD_ID = "localpack-bypass";
    public static final String REPO = "evgenmakarickij-wq/localpack-bypass";

    private static final String API_LATEST = "https://api.github.com/repos/" + REPO + "/releases/latest";
    private static final String MANIFEST_ASSET = "update.json";

    /** An unauthenticated GitHub API allows 60 requests per hour per address: one check a day is plenty. */
    private static final long CHECK_INTERVAL_MS = TimeUnit.HOURS.toMillis(24);

    private static final Pattern SHA256_HEX = Pattern.compile("^[0-9a-fA-F]{64}$");
    private static final Pattern SAFE_FILE_NAME = Pattern.compile("^[A-Za-z0-9._-]{1,128}$");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private static final ExecutorService IO = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "localpack-update");
        t.setDaemon(true);
        return t;
    });

    private static final Object STATE_LOCK = new Object();
    private static State state;

    private Updater() {}

    /** A newer release that is worth offering. */
    public record UpdateInfo(String version, String file, String sha256, String url, long size) {}

    /** What happened when the update was installed. */
    public record InstallResult(Kind kind, String version, String detail) {
        public enum Kind {
            /** New jar is in mods/ and the old jar is gone: the player only has to restart. */
            INSTALLED,
            /** New jar is verified and waiting; the old jar is removed when the game exits. */
            QUEUED,
            /** Running from a development environment, there is no jar to replace. */
            DEV,
            FAILED
        }
    }

    private static final class State {
        long lastCheckMs;
        String skippedVersion;
        /** Jar the game is running from, waiting to be deleted once it is no longer locked. */
        String pendingDelete;
        /** Verified jar sitting in the pending folder, waiting to be moved into mods/. */
        String pendingFrom;
        String pendingTo;
    }

    // ---------------------------------------------------------------- version

    public static String currentVersion() {
        return FabricLoader.getInstance().getModContainer(MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("0.0.0");
    }

    // ------------------------------------------------------------------ paths
    // mods/, config/localpack-bypass/ and the game directory behind them are resolved by GamePaths,
    // which hands back absolute normalised paths on Windows, macOS, Linux and Android alike.

    /** The single jar this mod was loaded from, or empty in a development environment. */
    private static Optional<Path> runningJar() {
        List<Path> paths = FabricLoader.getInstance().getModContainer(MOD_ID)
                .map(container -> container.getOrigin().getPaths())
                .orElse(List.of());
        if (paths.size() != 1) return Optional.empty();
        Path path = paths.get(0).toAbsolutePath().normalize();
        if (!path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")) return Optional.empty();
        return Files.isRegularFile(path) ? Optional.of(path) : Optional.empty();
    }

    // ------------------------------------------------------------------ state

    private static State state() {
        synchronized (STATE_LOCK) {
            if (state == null) state = load();
            return state;
        }
    }

    private static State load() {
        State fresh = new State();
        Path file = GamePaths.stateFile();
        if (!Files.isRegularFile(file)) return fresh;
        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            State loaded = GSON.fromJson(json, State.class);
            if (loaded == null) return fresh;
            // A field missing from an older state file stays null, which every caller treats as absent.
            return loaded;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read {}: {}", file, e.toString());
            return fresh;
        }
    }

    private static void save() {
        synchronized (STATE_LOCK) {
            if (state == null) return;
            try {
                Files.createDirectories(GamePaths.stateDir());
                Path target = GamePaths.stateFile();
                Path part = target.resolveSibling(target.getFileName() + ".part");
                Files.writeString(part, GSON.toJson(state), StandardCharsets.UTF_8);
                Files.move(part, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | RuntimeException e) {
                LOGGER.warn("Could not save the update state: {}", e.toString());
            }
        }
    }

    public static void skipVersion(String version) {
        synchronized (STATE_LOCK) {
            state().skippedVersion = version;
        }
        save();
    }

    /**
     * Runs at startup: drops bookkeeping for files that are already gone and finishes an install
     * that could not be completed while the game was running from the old jar.
     *
     * <p>The paths are read defensively. A game directory that is synced between devices can hold a
     * path this system cannot parse at all — a Windows literal opened on Android, say — and that
     * must not abort the client during initialisation.
     */
    public static void applyPendingCleanup() {
        State current = state();
        boolean changed = false;

        Optional<Path> running = GamePaths.parseStored(current.pendingDelete);
        if (current.pendingDelete != null && running.isEmpty()) {
            LOGGER.warn("Ignoring an unreadable path in the update state: {}", current.pendingDelete);
            current.pendingDelete = null;
            changed = true;
        } else if (running.isPresent() && !Files.exists(running.get())) {
            LOGGER.info("Old jar {} is gone, update finished", running.get());
            current.pendingDelete = null;
            changed = true;
            running = Optional.empty();
        }

        Optional<Path> from = GamePaths.parseStored(current.pendingFrom);
        Optional<Path> to = GamePaths.parseStored(current.pendingTo);
        if (from.isEmpty() || to.isEmpty()) {
            if (changed) save();
            return;
        }

        Path source = from.get();
        Path target = to.get();

        if (!Files.exists(source) && Files.exists(target)) {
            LOGGER.info("Update {} is in place", target.getFileName());
            current.pendingFrom = null;
            current.pendingTo = null;
            changed = true;
        } else if (Files.exists(source) && !Files.exists(target)) {
            // The move was refused last time only because the game was running from the old jar. On
            // Windows that stays true for as long as the process lives, so the queued delete is left
            // to the helper script; everywhere else the jar can be removed now, in this process,
            // which is what an Android launcher needs — it has no cmd.exe to fall back on.
            if (running.isPresent() && Platform.canReplaceRunningJar() && deleteQuietly(running.get())) {
                LOGGER.info("Removed the old jar {}", running.get());
                current.pendingDelete = null;
                changed = true;
            }

            // Two jars with the same mod id in mods/ would stop the next launch, so the move waits
            // until the old jar is really gone.
            if (current.pendingDelete == null) {
                try {
                    Files.createDirectories(target.getParent());
                    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("Installed the downloaded update as {}", target.getFileName());
                    current.pendingFrom = null;
                    current.pendingTo = null;
                    changed = true;
                } catch (IOException e) {
                    LOGGER.warn("Could not install the downloaded update: {}", e.toString());
                }
            }
        } else if (!Files.exists(source) && !Files.exists(target)) {
            // Both are gone: there is nothing left to finish, so stop retrying on every launch.
            LOGGER.info("{} and {} are both gone, dropping the queued update", source, target);
            current.pendingFrom = null;
            current.pendingTo = null;
            changed = true;
        }

        if (changed) save();
    }

    private static boolean deleteQuietly(Path file) {
        try {
            Files.deleteIfExists(file);
            return !Files.exists(file);
        } catch (IOException | RuntimeException e) {
            LOGGER.info("Could not remove {}: {}", file, e.toString());
            return false;
        }
    }

    // ------------------------------------------------------------- check

    /** Resolves to the newer release, or to null when there is nothing to offer. */
    public static CompletableFuture<UpdateInfo> check(boolean force) {
        return CompletableFuture.supplyAsync(() -> {
            State current = state();
            long now = System.currentTimeMillis();
            if (!force && now - current.lastCheckMs < CHECK_INTERVAL_MS) return null;

            try {
                UpdateInfo info = fetchLatest();
                current.lastCheckMs = now;
                save();
                if (info == null) return null;
                if (!ModVersion.isNewer(info.version(), currentVersion())) {
                    LOGGER.info("Release {} is not newer than {}", info.version(), currentVersion());
                    return null;
                }
                if (info.version().equals(current.skippedVersion)) {
                    LOGGER.info("Release {} was skipped by the player", info.version());
                    return null;
                }
                LOGGER.info("Update available: {} (running {})", info.version(), currentVersion());
                return info;
            } catch (IOException e) {
                LOGGER.warn("Could not check for updates: {}", e.toString());
                return null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }, IO);
    }

    private static UpdateInfo fetchLatest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(API_LATEST))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("User-Agent", MOD_ID + "/" + currentVersion())
                .GET()
                .build();
        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 404) {
            LOGGER.info("No releases published yet");
            return null;
        }
        if (response.statusCode() != 200) {
            throw new IOException("GitHub answered HTTP " + response.statusCode());
        }

        JsonObject release = JsonParser.parseString(response.body()).getAsJsonObject();
        String tag = string(release, "tag_name");
        if (tag == null) throw new IOException("The release has no tag");
        String version = tag.startsWith("v") || tag.startsWith("V") ? tag.substring(1) : tag;
        if (ModVersion.parse(version) == null) throw new IOException("Unreadable release tag: " + tag);

        JsonArray assets = release.has("assets") && release.get("assets").isJsonArray()
                ? release.getAsJsonArray("assets") : new JsonArray();

        // The manifest is authoritative for the file name and hash; the asset list only supplies the URL.
        JsonObject manifest = null;
        for (JsonElement element : assets) {
            if (!element.isJsonObject()) continue;
            String name = string(element.getAsJsonObject(), "name");
            if (MANIFEST_ASSET.equals(name)) {
                manifest = fetchManifest(string(element.getAsJsonObject(), "browser_download_url"));
                break;
            }
        }
        if (manifest == null) throw new IOException("The release has no " + MANIFEST_ASSET);

        String file = string(manifest, "file");
        String sha256 = string(manifest, "sha256");
        if (file == null || !SAFE_FILE_NAME.matcher(file).matches()) {
            throw new IOException("Unsafe asset name in " + MANIFEST_ASSET + ": " + file);
        }
        if (sha256 == null || !SHA256_HEX.matcher(sha256).matches()) {
            throw new IOException("Bad SHA-256 in " + MANIFEST_ASSET);
        }

        for (JsonElement element : assets) {
            if (!element.isJsonObject()) continue;
            JsonObject asset = element.getAsJsonObject();
            if (!file.equals(string(asset, "name"))) continue;
            String url = string(asset, "browser_download_url");
            requireGitHubUrl(url);
            long size = asset.has("size") ? asset.get("size").getAsLong() : 0L;
            return new UpdateInfo(version, file, sha256.toLowerCase(Locale.ROOT), url, size);
        }
        throw new IOException("The release does not contain " + file);
    }

    private static JsonObject fetchManifest(String url) throws IOException, InterruptedException {
        if (url == null) return null;
        requireGitHubUrl(url);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", MOD_ID + "/" + currentVersion())
                .GET()
                .build();
        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) throw new IOException("Manifest: HTTP " + response.statusCode());
        JsonElement parsed = JsonParser.parseString(response.body());
        if (!parsed.isJsonObject()) throw new IOException("Manifest is not an object");
        return parsed.getAsJsonObject();
    }

    private static String string(JsonObject object, String key) {
        if (object == null || !object.has(key) || !object.get(key).isJsonPrimitive()) return null;
        return object.get(key).getAsString();
    }

    /** Everything is pulled from github.com, and the hash is checked anyway: this is belt and braces. */
    private static void requireGitHubUrl(String url) throws IOException {
        if (url == null) throw new IOException("Missing download URL");
        URI uri;
        try {
            uri = URI.create(url);
        } catch (RuntimeException e) {
            throw new IOException("Malformed download URL: " + url, e);
        }
        String host = uri.getHost();
        if (!"https".equalsIgnoreCase(uri.getScheme()) || host == null
                || !(host.equalsIgnoreCase("github.com") || host.toLowerCase(Locale.ROOT).endsWith(".github.com"))) {
            throw new IOException("Refusing to download from " + url);
        }
    }

    // -------------------------------------------------------------- install

    /** Downloads, verifies and puts the new jar in place. Completes on {@link #IO}. */
    public static CompletableFuture<InstallResult> install(UpdateInfo info) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Path> running = runningJar();
            if (running.isEmpty()) {
                LOGGER.info("Running from a development environment, not installing {}", info.version());
                return new InstallResult(InstallResult.Kind.DEV, info.version(), "");
            }

            Path staged;
            try {
                Files.createDirectories(GamePaths.pendingDir());
                staged = download(info);
            } catch (IOException e) {
                LOGGER.error("Could not download update {}", info.version(), e);
                return new InstallResult(InstallResult.Kind.FAILED, info.version(), e.toString());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new InstallResult(InstallResult.Kind.FAILED, info.version(), e.toString());
            }

            Path target = GamePaths.modsDir().resolve(info.file());
            State current = state();
            current.pendingFrom = staged.toString();
            current.pendingTo = target.toString();
            current.pendingDelete = running.get().toString();
            current.skippedVersion = null;
            save();

            return removeRunningJar(info, running.get(), staged, target);
        }, IO);
    }

    private static Path download(UpdateInfo info) throws IOException, InterruptedException {
        Path part = GamePaths.pendingDir().resolve(info.file() + ".part");
        Path staged = GamePaths.pendingDir().resolve(info.file());
        Files.deleteIfExists(part);

        HttpRequest request = HttpRequest.newBuilder(URI.create(info.url()))
                .timeout(Duration.ofMinutes(10))
                .header("User-Agent", MOD_ID + "/" + currentVersion())
                .GET()
                .build();
        HttpResponse<InputStream> response = HTTP.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            response.body().close();
            throw new IOException("GitHub answered HTTP " + response.statusCode());
        }

        String actual;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream in = new DigestInputStream(response.body(), digest)) {
                Files.copy(in, part, StandardCopyOption.REPLACE_EXISTING);
            }
            actual = HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            Files.deleteIfExists(part);
            throw new IOException(e);
        } catch (IOException | RuntimeException e) {
            Files.deleteIfExists(part);
            throw e;
        }

        if (!actual.equalsIgnoreCase(info.sha256())) {
            Files.deleteIfExists(part);
            throw new IOException("SHA-256 mismatch: expected " + info.sha256() + ", got " + actual);
        }
        Files.move(part, staged, StandardCopyOption.REPLACE_EXISTING);
        LOGGER.info("Downloaded {} ({} bytes, sha256 verified)", info.file(), Files.size(staged));
        return staged;
    }

    /**
     * Deletes the jar the game is running from, then moves the verified update into {@code mods/}.
     *
     * <p>If the jar cannot be deleted, installing would leave two jars with the same mod id in
     * {@code mods/} and Fabric would refuse the next launch with a duplicate mod error. So the
     * update is queued instead: on Windows a small detached script waits for this process to exit
     * and does both steps, while on Android, Linux and macOS — where the running jar is not locked
     * and the delete normally succeeds outright — the next launch finishes the job in
     * {@link #applyPendingCleanup()}.
     */
    private static InstallResult removeRunningJar(UpdateInfo info, Path running, Path staged, Path target) {
        try {
            Files.delete(running);
            Files.createDirectories(target.getParent());
            Files.move(staged, target, StandardCopyOption.REPLACE_EXISTING);
            State current = state();
            current.pendingFrom = null;
            current.pendingTo = null;
            current.pendingDelete = null;
            save();
            LOGGER.info("Installed {}; the old jar is gone", info.file());
            return new InstallResult(InstallResult.Kind.INSTALLED, info.version(), "");
        } catch (IOException | RuntimeException e) {
            LOGGER.info("Cannot remove {} while the game is running: {}", running, e.toString());
        }

        if (spawnHelper(running, staged, target)) {
            LOGGER.info("Queued {} for installation when the game exits", info.file());
            return new InstallResult(InstallResult.Kind.QUEUED, info.version(), "");
        }

        // No helper available: keep the verified jar, but say where it is so it can be moved by hand.
        return new InstallResult(InstallResult.Kind.QUEUED, info.version(), staged.toString());
    }

    /**
     * Last resort for Windows, where a running jar is locked. Writes a short script next to the
     * state file and starts it detached; the script polls this process id and then performs the
     * delete and the move. Refuses to build the script if any path contains a command-shell
     * metacharacter, so a path can never turn into something the shell would execute.
     */
    private static boolean spawnHelper(Path running, Path staged, Path target) {
        if (!Platform.isWindows()) {
            // On Android, Linux and macOS the running jar is only open, not locked, so the delete
            // above failed for a reason no helper can work around. The queued install is finished by
            // applyPendingCleanup() on the next launch instead.
            return false;
        }

        List<Path> paths = List.of(running, staged, target);
        for (Path path : paths) {
            String text = path.toAbsolutePath().toString();
            if (text.chars().anyMatch(c -> c == '%' || c == '"' || c == '!' || c == '^'
                    || c == '&' || c == '|' || c == '<' || c == '>' || c == '\n' || c == '\r')) {
                LOGGER.warn("Not writing an update helper: unsupported character in {}", text);
                return false;
            }
        }

        long pid = ProcessHandle.current().pid();
        String script = "@echo off\r\n"
                + "rem Written by localpack-bypass to finish an update after the game exits.\r\n"
                + ":wait\r\n"
                + "tasklist /FI \"PID eq " + pid + "\" 2>NUL | find \"" + pid + "\" >NUL\r\n"
                + "if not errorlevel 1 (\r\n"
                + "  timeout /t 2 /nobreak >NUL\r\n"
                + "  goto wait\r\n"
                + ")\r\n"
                + "del /f /q \"" + running.toAbsolutePath() + "\"\r\n"
                + "if not exist \"" + target.getParent().toAbsolutePath() + "\" mkdir \"" + target.getParent().toAbsolutePath() + "\"\r\n"
                + "move /y \"" + staged.toAbsolutePath() + "\" \"" + target.toAbsolutePath() + "\"\r\n"
                + "del /f /q \"%~f0\"\r\n";

        try {
            Files.createDirectories(GamePaths.stateDir());
            Path scriptFile = GamePaths.stateDir().resolve("finish-update-" + pid + ".cmd");
            Files.writeString(scriptFile, script, StandardCharsets.UTF_8);
            new ProcessBuilder("cmd.exe", "/c", "start", "\"localpack-bypass update\"", "/min",
                    scriptFile.toAbsolutePath().toString())
                    .directory(GamePaths.stateDir().toFile())
                    .start();
            return true;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not start the update helper: {}", e.toString());
            return false;
        }
    }
}
