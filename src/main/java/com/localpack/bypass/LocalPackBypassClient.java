package com.localpack.bypass;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalPackBypassClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    @Override
    public void onInitializeClient() {
        // Which system this is decides how a running jar is replaced and what the launcher's game
        // directory looks like, so both end up in the log next to the folders themselves.
        LOGGER.info("Local Resource Pack Cache loaded on {} (game directory {}, mods {}, config {})",
                Platform.current(), GamePaths.gameDir(), GamePaths.modsDir(), GamePaths.configDir());

        // Read now, so a damaged settings file is reported at startup rather than in the middle of a
        // join, and write the memory out on the way down so the last few seconds are not lost.
        Config.get();
        PackRegistry.flush();
        Runtime.getRuntime().addShutdownHook(new Thread(PackRegistry::flush, "localpack-history-save"));

        // Finish an update that was downloaded on a previous run, then offer any newer release.
        Updater.applyPendingCleanup();
        UpdateFlow.init();
    }
}
