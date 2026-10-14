package com.localpack.bypass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * The player's settings, stored as JSON in {@code config/localpack-bypass/config.json}.
 *
 * <p>Every field has a working default, so a missing or damaged file is not an error: the defaults
 * are used and the file is written again on the next change. Fields are read through the accessors
 * rather than directly, because each one is clamped to a range the mod can actually work with — a
 * hand-edited file must not be able to hand a negative timeout to a download.
 */
public final class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object LOCK = new Object();

    // ------------------------------------------------------------------ ranges

    public static final int MIN_BATCH_WAIT_MS = 0;
    public static final int MAX_BATCH_WAIT_MS = 5_000;
    public static final int MIN_TIMEOUT_SECONDS = 10;
    public static final int MAX_TIMEOUT_SECONDS = 3_600;
    public static final int MIN_ATTEMPTS = 1;
    public static final int MAX_ATTEMPTS = 10;
    public static final int MIN_HISTORY = 20;
    public static final int MAX_HISTORY = 2_000;

    // ------------------------------------------------------------------ fields

    /** How long to wait for more packs after the first one arrives, so a batch is asked about once. */
    private int batchWaitMs = 300;

    /** How long a single pack download may take before it is given up on. */
    private int downloadTimeoutSeconds = 600;

    /** How many times a failed download is retried before the player is told it did not work. */
    private int downloadAttempts = 3;

    /** Skip the question for a server the player has already downloaded packs from. */
    private boolean rememberServers = true;

    /** Leave packs from other servers enabled when a new server's packs are applied. */
    private boolean keepEarlierPacks = true;

    /** Record what every server sent, for the history screens. */
    private boolean historyEnabled = true;

    /** How many entries the history keeps before the oldest are dropped. */
    private int historyLimit = 300;

    /** Copy the diagnostic detail the mod logs into the history as well. */
    private boolean verboseLog = false;

    /** Folder downloaded packs are written to. Empty means the game's own resourcepacks folder. */
    private String resourcePackDir = "";

    private static Config instance;

    private Config() {}

    // ------------------------------------------------------------------ access

    public static Config get() {
        synchronized (LOCK) {
            if (instance == null) instance = load();
            return instance;
        }
    }

    private static Config load() {
        Path file = GamePaths.configFile();
        if (!Files.isRegularFile(file)) return new Config();
        try {
            Config loaded = GSON.fromJson(Files.readString(file, StandardCharsets.UTF_8), Config.class);
            // A file that parses but is not an object (say a bare string) comes back as null.
            return loaded == null ? new Config() : loaded;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read {}: {}. Using defaults.", file, e.toString());
            return new Config();
        }
    }

    /** Writes the file, replacing the old one only if the new one is complete. */
    public static void save() {
        Path file = GamePaths.configFile();
        try {
            Files.createDirectories(GamePaths.stateDir());
            Path part = file.resolveSibling(file.getFileName() + ".part");
            synchronized (LOCK) {
                Files.writeString(part, GSON.toJson(get()), StandardCharsets.UTF_8);
            }
            Files.move(part, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not save {}: {}", file, e.toString());
        }
    }

    // ------------------------------------------------------------------ values

    public int batchWaitMs() {
        return clamp(batchWaitMs, MIN_BATCH_WAIT_MS, MAX_BATCH_WAIT_MS);
    }

    public void batchWaitMs(int value) {
        batchWaitMs = clamp(value, MIN_BATCH_WAIT_MS, MAX_BATCH_WAIT_MS);
    }

    public int downloadTimeoutSeconds() {
        return clamp(downloadTimeoutSeconds, MIN_TIMEOUT_SECONDS, MAX_TIMEOUT_SECONDS);
    }

    public void downloadTimeoutSeconds(int value) {
        downloadTimeoutSeconds = clamp(value, MIN_TIMEOUT_SECONDS, MAX_TIMEOUT_SECONDS);
    }

    public int downloadAttempts() {
        return clamp(downloadAttempts, MIN_ATTEMPTS, MAX_ATTEMPTS);
    }

    public void downloadAttempts(int value) {
        downloadAttempts = clamp(value, MIN_ATTEMPTS, MAX_ATTEMPTS);
    }

    public boolean rememberServers() {
        return rememberServers;
    }

    public void rememberServers(boolean value) {
        rememberServers = value;
    }

    public boolean keepEarlierPacks() {
        return keepEarlierPacks;
    }

    public void keepEarlierPacks(boolean value) {
        keepEarlierPacks = value;
    }

    public boolean historyEnabled() {
        return historyEnabled;
    }

    public void historyEnabled(boolean value) {
        historyEnabled = value;
    }

    public int historyLimit() {
        return clamp(historyLimit, MIN_HISTORY, MAX_HISTORY);
    }

    public void historyLimit(int value) {
        historyLimit = clamp(value, MIN_HISTORY, MAX_HISTORY);
    }

    public boolean verboseLog() {
        return verboseLog;
    }

    public void verboseLog(boolean value) {
        verboseLog = value;
    }

    /** The typed folder: empty means "use the game's resourcepacks folder". */
    public String resourcePackDir() {
        return resourcePackDir == null ? "" : resourcePackDir.trim();
    }

    public void resourcePackDir(String value) {
        resourcePackDir = value == null ? "" : value.trim();
    }

    // ------------------------------------------------------------------ folder

    /**
     * The folder downloaded packs go into: the one the player named, or the game's own. A relative
     * path is resolved against the game directory, so "my packs" on the config screen means the
     * same as it would in the launcher's folder rather than the process's working directory.
     */
    public static Path packDir() {
        String typed = get().resourcePackDir();
        if (typed.isEmpty()) return GamePaths.resourcePacksDir();
        return resolve(typed).orElseGet(GamePaths::resourcePacksDir);
    }

    /** Absolute, or relative to the game directory. Empty when the text cannot be a path at all. */
    public static Optional<Path> resolve(String typed) {
        if (typed == null || typed.isBlank()) return Optional.empty();
        Optional<Path> parsed = GamePaths.parseStored(typed);
        if (parsed.isEmpty()) return Optional.empty();
        Path path = parsed.get();
        return Optional.of(path.isAbsolute() ? path.normalize() : GamePaths.gameDir().resolve(path).normalize());
    }

    /**
     * Checks a typed folder and creates it if needed. Returns null when the folder is usable, or a
     * translation key explaining what is wrong with it — the config screen shows that to the player
     * instead of silently keeping a folder that would never work.
     */
    public static String problem(String typed, boolean create) {
        if (typed == null || typed.isBlank()) return null; // empty means "use the default"
        Optional<Path> resolved = resolve(typed);
        if (resolved.isEmpty()) return "localpack-bypass.config.folder.badPath";
        Path path = resolved.get();
        if (Files.isRegularFile(path)) return "localpack-bypass.config.folder.isFile";
        if (create) {
            try {
                Files.createDirectories(path);
            } catch (IOException | RuntimeException e) {
                LOGGER.warn("Could not create {}: {}", path, e.toString());
                return "localpack-bypass.config.folder.cannotCreate";
            }
        }
        if (Files.exists(path) && !Files.isWritable(path)) return "localpack-bypass.config.folder.notWritable";
        return null;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
