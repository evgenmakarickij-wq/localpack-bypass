package com.localpack.bypass;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalPackBypassClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Local Resource Pack Cache loaded");

        // Finish an update that was downloaded on a previous run, then offer any newer release.
        Updater.applyPendingCleanup();
        UpdateFlow.init();
    }
}
