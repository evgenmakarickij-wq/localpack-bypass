package com.localpack.bypass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** SHA-1 lookup for persistent resource-pack archives. Network downloads remain vanilla-owned. */
public final class LocalPackBypassHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    public static final String AUTO_FILE_PREFIX = "localpack-";

    private static final Pattern SHA1_HEX = Pattern.compile("^[0-9a-fA-F]{40}$");
    private static final Map<Path, CachedHash> CACHE = new ConcurrentHashMap<>();
    private static final long CACHE_MIN_AGE_MS = 3_000L;

    private record CachedHash(long size, long modified, String sha1) {}

    private LocalPackBypassHelper() {}

    public static boolean isValidSha1(String hash) {
        return hash != null && SHA1_HEX.matcher(hash).matches();
    }

    public static String fileNameFor(String hash) {
        return AUTO_FILE_PREFIX + hash.toLowerCase(Locale.ROOT) + ".zip";
    }

    public static Path findLocalPackByHash(Path dir, String targetHash) {
        if (!isValidSha1(targetHash) || dir == null || !Files.isDirectory(dir)) return null;

        Path named = dir.resolve(fileNameFor(targetHash));
        if (Files.isRegularFile(named)) {
            String sha1 = cachedSha1(named);
            if (sha1.equalsIgnoreCase(targetHash)) return named;
            LOGGER.warn("{} does not match its file name; searching the folder", named.getFileName());
        }

        try (Stream<Path> stream = Files.list(dir)) {
            for (Path file : (Iterable<Path>) stream::iterator) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (!name.endsWith(".zip") || !Files.isRegularFile(file)) continue;
                if (cachedSha1(file).equalsIgnoreCase(targetHash)) return file;
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.debug("Could not search {} for resource packs: {}", dir, e.toString());
        }
        return null;
    }

    private static String cachedSha1(Path file) {
        try {
            Path key = file.toAbsolutePath().normalize();
            long size = Files.size(key);
            long modified = Files.getLastModifiedTime(key).toMillis();
            CachedHash cached = CACHE.get(key);
            if (cached != null && cached.size() == size && cached.modified() == modified) {
                return cached.sha1();
            }
            String sha1 = getSha1(key);
            if (!sha1.isEmpty() && System.currentTimeMillis() - modified > CACHE_MIN_AGE_MS) {
                CACHE.put(key, new CachedHash(size, modified, sha1));
            }
            return sha1;
        } catch (IOException | RuntimeException e) {
            return "";
        }
    }

    public static String getSha1(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[65536];
            int read;
            while ((read = in.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException | NoSuchAlgorithmException | RuntimeException e) {
            return "";
        }
    }
}
