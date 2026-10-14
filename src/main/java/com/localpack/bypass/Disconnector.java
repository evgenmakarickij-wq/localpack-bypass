package com.localpack.bypass;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

/** Implemented by the mixin on ClientCommonPacketListenerImpl to reach its protected connection. */
public interface Disconnector {
    void localpackBypass$disconnect(Component reason);

    /** Identity of the network connection (shared by the configuration and play phases). */
    Object localpackBypass$connectionKey();

    /** The saved-server entry behind this connection, or null when the player connected directly. */
    ServerData localpackBypass$serverData();
}
