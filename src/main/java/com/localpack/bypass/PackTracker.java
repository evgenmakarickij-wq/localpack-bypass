package com.localpack.bypass;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;

import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Associates vanilla download results with the server that offered them for history and UI only. */
public final class PackTracker {

    private record Offered(String server, String url, String hash) {}

    private static final ConcurrentHashMap<UUID, Offered> OFFERS = new ConcurrentHashMap<>();

    private PackTracker() {}

    public static void observe(ServerData serverData, ClientboundResourcePackPushPacket packet) {
        String server = serverData == null || serverData.ip == null
                ? ""
                : serverData.ip.trim().toLowerCase(Locale.ROOT);
        OFFERS.put(packet.id(), new Offered(server, packet.url(), packet.hash()));
    }

    public static void forget(UUID id) {
        OFFERS.remove(id);
    }

    public static void clear() {
        OFFERS.clear();
    }

    public static void completed(UUID id, Path downloaded, boolean cacheHit) {
        Offered offered = OFFERS.get(id);
        try {
            String expected = offered == null ? "" : offered.hash();
            PackCache.Archived archived = PackCache.archive(downloaded, expected);
            String server = offered == null ? "" : offered.server();
            String url = offered == null ? "" : offered.url();
            PackRegistry.remember(archived.hash(), archived.fileName(), archived.bytes(), server, url);
            PackRegistry.event(server, url, archived.hash(), cacheHit ? "found" : "downloaded",
                    null, archived.bytes(), 0L);
        } catch (Exception e) {
            LocalPackBypassClient.LOGGER.warn("Could not archive server resource pack {}: {}", id, e.toString());
        }
    }

    public static void failed(UUID id) {
        Offered offered = OFFERS.get(id);
        if (offered != null) {
            PackRegistry.event(offered.server(), offered.url(), offered.hash(), "failed",
                    "vanilla download failed", 0L, 0L);
        }
    }
}
