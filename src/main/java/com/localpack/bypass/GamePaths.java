package com.localpack.bypass;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Every folder and file the mod touches, resolved in one place.
 *
 * <p>All of it hangs off the launcher's game directory, which Fabric resolves the same way for
 * Windows, macOS, Linux and the Android launchers (PojavLauncher and its forks); nothing here is
 * assembled from a platform name or a hard-coded separator. What this class does guarantee is that
 * every path comes back absolute and normalised before it is logged, compared or written into the
 * update state, that the resource pack folder is asked of the game rather than guessed, and that a
 * path stored on one system cannot blow up the client when it is read back on another.
 */
public final class GamePaths {

    private GamePaths() {}

    /** The folder everything else is relative to: {@code .minecraft}, or the launcher's equivalent. */
    public static Path gameDir() {
        return FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
    }

    /** Fabric scans this folder for {@code *.jar} on every launch. */
    public static Path modsDir() {
        return gameDir().resolve("mods");
    }

    public static Path configDir() {
        return FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
    }

    /**
     * The folder the pack repository reads. It is asked of the game rather than derived, because the
     * selection in options.txt refers to this exact path; the game directory is only the fallback
     * for the moment before a client exists.
     */
    public static Path resourcePacksDir() {
        Minecraft client = Minecraft.getInstance();
        Path dir = client == null ? null : client.getResourcePackDirectory();
        return dir == null ? gameDir().resolve("resourcepacks") : dir.toAbsolutePath().normalize();
    }

    public static Path stateDir() {
        return configDir().resolve(Updater.MOD_ID);
    }

    public static Path stateFile() {
        return stateDir().resolve("update-state.json");
    }

    /** The player's settings, written by the config screen. */
    public static Path configFile() {
        return stateDir().resolve("config.json");
    }

    /** Everything the mod remembers about packs and servers, written by {@link PackRegistry}. */
    public static Path historyFile() {
        return stateDir().resolve("history.json");
    }

    public static Path pendingDir() {
        return stateDir().resolve("pending");
    }

    /**
     * Reads a path an earlier run wrote down. A game directory that is synced between devices can
     * leave a literal this system cannot parse at all — the classic case being a Windows path opened
     * on Android — and {@link Path#of} rejects those with an unchecked exception, so callers get an
     * empty result instead of a crash during client initialisation.
     */
    public static Optional<Path> parseStored(String text) {
        if (text == null || text.isBlank()) return Optional.empty();
        try {
            return Optional.of(Path.of(text).toAbsolutePath().normalize());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}
