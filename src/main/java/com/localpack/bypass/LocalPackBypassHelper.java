package com.localpack.bypass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LocalPackBypassHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    public static final String AUTO_FILE_PREFIX = "localpack-";

    private static final Pattern SHA1_HEX = Pattern.compile("^[0-9a-fA-F]{40}$");

    private record CachedHash(long size, long modified, String sha1) {}

    private static final Map<Path, CachedHash> CACHE = new ConcurrentHashMap<>();

    /** Files modified more recently than this are not cached: they might still be being written. */
    private static final long CACHE_MIN_AGE_MS = 3_000L;

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private LocalPackBypassHelper() {}

    public static boolean isValidSha1(String hash) {
        return hash != null && SHA1_HEX.matcher(hash).matches();
    }

    public static String fileNameFor(String hash) {
        return AUTO_FILE_PREFIX + hash.toLowerCase(Locale.ROOT) + ".zip";
    }

    /**
     * The local zip holding this pack, or null.
     *
     * <p>The file this mod writes for a pack is named after the server's own SHA-1, so the file
     * that should be there is known without opening anything: that path is tried first and its
     * hash confirmed, which is one file read instead of one read per zip in the folder. Only when
     * that misses does the folder get searched, for a pack that was renamed or downloaded by hand.
     */
    public static Path findLocalPackByHash(Path dir, String targetHash) {
        if (!isValidSha1(targetHash) || dir == null || !Files.isDirectory(dir)) return null;

        Path named = dir.resolve(fileNameFor(targetHash));
        if (Files.isRegularFile(named)) {
            String sha1 = cachedSha1(named);
            if (sha1.equalsIgnoreCase(targetHash)) return named;
            LOGGER.warn("{} does not hold the pack it is named after ({} instead of {}); searching the folder",
                    named.getFileName(), sha1.isEmpty() ? "unreadable" : sha1, targetHash);
        }

        try (Stream<Path> stream = Files.list(dir)) {
            for (Path file : (Iterable<Path>) stream::iterator) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (!name.endsWith(".zip") || !Files.isRegularFile(file)) continue;

                String sha1 = cachedSha1(file);
                if (!sha1.isEmpty() && sha1.equalsIgnoreCase(targetHash)) {
                    return file;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private static String cachedSha1(Path file) {
        try {
            long size = Files.size(file);
            long modified = Files.getLastModifiedTime(file).toMillis();
            CachedHash cached = CACHE.get(file);
            if (cached != null && cached.size() == size && cached.modified() == modified) {
                return cached.sha1();
            }
            String sha1 = getSha1WithRetry(file);
            boolean settled = System.currentTimeMillis() - modified > CACHE_MIN_AGE_MS;
            if (!sha1.isEmpty() && settled) {
                CACHE.put(file, new CachedHash(size, modified, sha1));
            }
            return sha1;
        } catch (IOException e) {
            return "";
        }
    }

    /** A file can be briefly unreadable (antivirus, the game itself): retry before giving up. */
    private static String getSha1WithRetry(Path file) {
        for (int attempt = 0; attempt < 3; attempt++) {
            String sha1 = getSha1(file);
            if (!sha1.isEmpty()) return sha1;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "";
            }
        }
        return "";
    }

    /** For the log: every zip in the folder with its size and (already known) SHA-1. Does not hash anything. */
    public static String describeZips(Path dir) {
        if (dir == null || !Files.isDirectory(dir)) return "(folder not found)";
        StringBuilder sb = new StringBuilder();
        try (Stream<Path> stream = Files.list(dir)) {
            for (Path file : (Iterable<Path>) stream::iterator) {
                String name = file.getFileName().toString();
                if (!name.toLowerCase(Locale.ROOT).endsWith(".zip") || !Files.isRegularFile(file)) continue;
                CachedHash cached = CACHE.get(file);
                if (sb.length() > 0) sb.append("; ");
                sb.append(name).append(" [").append(Files.size(file)).append(" B, sha1=")
                        .append(cached == null ? "unknown" : cached.sha1()).append("]");
            }
        } catch (IOException e) {
            return "(could not list folder: " + e.getMessage() + ")";
        }
        return sb.length() == 0 ? "(no zip files)" : sb.toString();
    }

    public static String getSha1(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[65536];
            int n;
            while ((n = in.read(buffer)) != -1) {
                digest.update(buffer, 0, n);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            return "";
        }
    }

    /** What a finished download produced: the name it was saved under, and its size. */
    public record Downloaded(String fileName, long bytes) {}

    /** A download with the settings the mod has always used. */
    public static String download(String url, String expectedHash, Path dir) throws IOException, InterruptedException {
        return download(url, expectedHash, dir, Duration.ofMinutes(10), 1).fileName();
    }

    /**
     * Fetches a pack into {@code dir} and checks it against the hash the server gave.
     *
     * <p>A download that fails is retried, because the failures that actually happen in the field
     * are the transient ones: a proxy that drops the connection, a phone that switches network, a
     * mirror that answers 503. A wrong hash is retried too — that is what a truncated transfer looks
     * like. What is not retried is an answer that will be the same every time: a 404, a 403, or a URL
     * that is not a URL.
     */
    public static Downloaded download(String url, String expectedHash, Path dir, Duration timeout, int attempts)
            throws IOException, InterruptedException {
        URI uri = parseHttpUri(url);
        Files.createDirectories(dir);
        String fileName = fileNameFor(expectedHash);
        Path target = dir.resolve(fileName);
        Path part = dir.resolve(fileName + ".part");

        int tries = Math.max(1, attempts);
        for (int attempt = 1; ; attempt++) {
            try {
                long bytes = downloadOnce(uri, expectedHash, part, timeout);
                Files.move(part, target, StandardCopyOption.REPLACE_EXISTING);
                return new Downloaded(fileName, bytes);
            } catch (IOException e) {
                Files.deleteIfExists(part);
                if (attempt >= tries || !worthRetrying(e)) throw e;
                LOGGER.warn("Download of {} did not work (attempt {}/{}): {}", url, attempt, tries, e.toString());
                Thread.sleep(Math.min(2_000L, 250L * attempt));
            } finally {
                Files.deleteIfExists(part);
            }
        }
    }

    private static URI parseHttpUri(String url) {
        URI uri;
        try {
            uri = URI.create(url);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Некорректный URL: " + url, e);
        }
        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            throw new IllegalArgumentException("Неподдерживаемая схема URL: " + scheme);
        }
        return uri;
    }

    /** Writes to {@code part} and returns the number of bytes read. */
    private static long downloadOnce(URI uri, String expectedHash, Path part, Duration timeout)
            throws IOException, InterruptedException {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(timeout)
                    .header("User-Agent", "localpack-bypass")
                    .GET()
                    .build();
            HttpResponse<InputStream> response = HTTP.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                response.body().close();
                throw new HttpStatusException(response.statusCode());
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            long bytes;
            try (InputStream in = new DigestInputStream(response.body(), digest)) {
                bytes = Files.copy(in, part, StandardCopyOption.REPLACE_EXISTING);
            }

            String actual = HexFormat.of().formatHex(digest.digest());
            if (!actual.equalsIgnoreCase(expectedHash)) {
                throw new IOException("SHA-1 не совпадает: ожидался " + expectedHash + ", получен " + actual);
            }
            return bytes;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

    /** An HTTP answer that is not 200. Kept apart from other errors so it can be judged for retrying. */
    private static final class HttpStatusException extends IOException {
        private final int status;

        HttpStatusException(int status) {
            super("Сервер ответил HTTP " + status);
            this.status = status;
        }

        int status() {
            return status;
        }
    }

    private static boolean worthRetrying(IOException e) {
        if (e instanceof HttpStatusException status) {
            // 4xx means the request itself is the problem, except for the two that mean "ask again".
            int code = status.status();
            return code >= 500 || code == 408 || code == 429;
        }
        // Timeouts, resets and wrong hashes: the next attempt may well be fine.
        return true;
    }
}
