package com.localpack.bypass;

import dev.mcchopik.modulib.io.AtomicFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/** Bridges the player's persistent pack folder and Minecraft's verified server-pack cache. */
public final class PackCache {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private PackCache() {}

    /**
     * Seeds the exact location vanilla will check. Vanilla hashes this copy again before using it,
     * so a stale or changed file falls through to the normal HTTP download.
     */
    public static boolean seed(Path vanillaTarget, String expectedHash) {
        Path local = find(expectedHash);
        if (local == null) return false;
        try {
            AtomicFiles.copy(local, vanillaTarget);
            LOGGER.info("Seeded Minecraft's server-pack cache from {}", local.getFileName());
            return true;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not seed the server-pack cache from {}: {}", local, e.toString());
            return false;
        }
    }

    /** Stores a vanilla-verified download without enabling it in the normal resource-pack list. */
    public static Archived archive(Path downloaded, String hash) throws IOException {
        if (!LocalPackBypassHelper.isValidSha1(hash)) {
            hash = LocalPackBypassHelper.getSha1(downloaded);
        }
        if (!LocalPackBypassHelper.isValidSha1(hash)) {
            throw new IOException("Could not calculate SHA-1 for " + downloaded);
        }

        String normalized = hash.toLowerCase(java.util.Locale.ROOT);
        String fileName = LocalPackBypassHelper.fileNameFor(normalized);
        Path gameTarget = PackStorage.gameDir().resolve(fileName).toAbsolutePath().normalize();
        AtomicFiles.copy(downloaded, gameTarget);

        Path configuredTarget = PackStorage.downloadDir().resolve(fileName).toAbsolutePath().normalize();
        if (!configuredTarget.equals(gameTarget)) {
            try {
                AtomicFiles.copy(downloaded, configuredTarget);
            } catch (IOException | RuntimeException e) {
                LOGGER.warn("Could not write the extra archive copy to {}: {}", configuredTarget, e.toString());
            }
        }
        return new Archived(normalized, fileName, Files.size(downloaded));
    }

    private static Path find(String hash) {
        Set<Path> dirs = new LinkedHashSet<>();
        dirs.add(PackStorage.downloadDir().toAbsolutePath().normalize());
        dirs.add(PackStorage.gameDir().toAbsolutePath().normalize());
        for (Path dir : dirs) {
            Path match = LocalPackBypassHelper.findLocalPackByHash(dir, hash);
            if (match != null) return match;
        }
        return null;
    }

    public record Archived(String hash, String fileName, long bytes) {}
}
