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

public final class LocalPackBypassHelper {

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

    public static Path findLocalPackByHash(Path dir, String targetHash) {
        if (!isValidSha1(targetHash) || dir == null || !Files.isDirectory(dir)) return null;

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

    public static String download(String url, String expectedHash, Path dir) throws IOException, InterruptedException {
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

        Files.createDirectories(dir);
        String fileName = fileNameFor(expectedHash);
        Path target = dir.resolve(fileName);
        Path part = dir.resolve(fileName + ".part");

        try {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofMinutes(10))
                    .header("User-Agent", "localpack-bypass")
                    .GET()
                    .build();
            HttpResponse<InputStream> response = HTTP.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                response.body().close();
                throw new IOException("Сервер ответил HTTP " + response.statusCode());
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            try (InputStream in = new DigestInputStream(response.body(), digest)) {
                Files.copy(in, part, StandardCopyOption.REPLACE_EXISTING);
            }

            String actual = HexFormat.of().formatHex(digest.digest());
            if (!actual.equalsIgnoreCase(expectedHash)) {
                throw new IOException("SHA-1 не совпадает: ожидался " + expectedHash + ", получен " + actual);
            }

            Files.move(part, target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        } finally {
            Files.deleteIfExists(part);
        }
    }
}
