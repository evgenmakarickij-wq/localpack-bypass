package com.localpack.bypass;

import java.nio.file.Path;

/**
 * Where downloaded packs live.
 *
 * <p>The player can keep packs wherever they like (see {@link Config#resourcePackDir()}), which is
 * useful on phones, where the folder the game was installed into is awkward to reach with a file
 * manager. A copy is also archived in the game's resourcepacks folder so it remains visible in the
 * normal resource-pack screen. Archiving never selects or enables that copy.
 */
public final class PackStorage {

    private PackStorage() {}

    /** The folder downloads are written to and looked for in. */
    public static Path downloadDir() {
        return Config.packDir();
    }

    /** The folder the game loads packs from. */
    public static Path gameDir() {
        return GamePaths.resourcePacksDir();
    }
}
