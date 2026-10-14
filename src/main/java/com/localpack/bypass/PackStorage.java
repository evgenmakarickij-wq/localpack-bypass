package com.localpack.bypass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Where downloaded packs live, and how they get in front of the game.
 *
 * <p>The player can keep packs wherever they like (see {@link Config#resourcePackDir()}), which is
 * useful on phones, where the folder the game was installed into is awkward to reach with a file
 * manager. The game reads only its own resourcepacks folder, though — pack selection in options.txt
 * is a list of names in that folder — so a pack kept elsewhere is copied into that folder, under the
 * same name, before it is switched on. The name is derived from the pack's own SHA-1, so a file
 * already there with that name is the same pack and no copy is made.
 */
public final class PackStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    private PackStorage() {}

    /** The folder downloads are written to and looked for in. */
    public static Path downloadDir() {
        return Config.packDir();
    }

    /** The folder the game loads packs from. */
    public static Path gameDir() {
        return GamePaths.resourcePacksDir();
    }

    /**
     * Makes sure the game can load this pack: does nothing when the pack is already in the game's own
     * folder, and copies it there when it is not.
     */
    public static void ensureLoadable(Path file) throws IOException {
        Path game = gameDir().toAbsolutePath().normalize();
        Path target = game.resolve(file.getFileName().toString());
        if (target.equals(file.toAbsolutePath().normalize())) return;
        try {
            if (Files.isRegularFile(target) && Files.size(target) == Files.size(file)) return;
        } catch (IOException e) {
            // Unreadable: fall through and copy over it, which is also the way to repair it.
        }
        Files.createDirectories(game);
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
        LOGGER.info("Copied {} into the game's resourcepacks folder so it can be loaded",
                file.getFileName());
    }
}
