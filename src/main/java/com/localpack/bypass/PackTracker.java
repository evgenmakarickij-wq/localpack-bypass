package com.localpack.bypass;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;

import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Associates vanilla download results with the server that offered them for history and UI only. */
public final class PackTracker {

    private record OfferKey(UUID id, String hash) {}
    private record Offered(String server, String url) {}

    private static final ConcurrentHashMap<OfferKey, Offered> OFFERS = new ConcurrentHashMap<>();
    private record Archived(String hash, long generation) {}
    private static final ConcurrentHashMap<UUID, Archived> ARCHIVED = new ConcurrentHashMap<>();

    private PackTracker() {}

    public static void observe(ServerData serverData, ClientboundResourcePackPushPacket packet) {
        String server = serverData == null || serverData.ip == null
                ? ""
                : serverData.ip.trim().toLowerCase(Locale.ROOT);
        forget(packet.id());
        OFFERS.put(key(packet.id(), packet.hash()), new Offered(server, packet.url()));
    }

    public static void forget(UUID id) {
        OFFERS.keySet().removeIf(key -> key.id().equals(id));
        ARCHIVED.remove(id);
    }

    public static void clear() {
        OFFERS.clear();
        ARCHIVED.clear();
    }

    public static void completed(UUID id, Path downloaded, String verifiedHash, boolean cacheHit, long generation) {
        Offered offered = OFFERS.get(key(id, verifiedHash));
        PersistentPacks.runIfCurrent(generation, () -> {
            try {
                PackCache.Archived archived = PackCache.archive(downloaded, verifiedHash);
                String server = offered == null ? "" : offered.server();
                String url = offered == null ? "" : offered.url();
                PackRegistry.remember(archived.hash(), archived.fileName(), archived.bytes(), server, url);
                ARCHIVED.put(id, new Archived(archived.hash(), generation));
                PackRegistry.event(server, url, archived.hash(), cacheHit ? "found" : "downloaded",
                        null, archived.bytes(), 0L);
            } catch (Exception e) {
                LocalPackBypassClient.LOGGER.warn("Could not archive server resource pack {}: {}", id, e.toString());
            }
        });
    }

    public static void failed(UUID id, String hash, long generation) {
        Offered offered = OFFERS.get(key(id, hash));
        PersistentPacks.runIfCurrent(generation, () -> {
            if (offered == null) return;
            PackRegistry.event(offered.server(), offered.url(), hash, "failed",
                    "vanilla download failed", 0L, 0L);
        });
    }

    public static void successful(UUID id) {
        Archived archived = ARCHIVED.remove(id);
        if (archived != null) {
            PersistentPacks.runIfCurrent(archived.generation(), () -> {
                PackRegistry.markPendingEnable(archived.hash());
                PersistentPacks.queue(archived.hash());
            });
        }
        OFFERS.keySet().stream().filter(key -> key.id().equals(id)).findFirst().ifPresent(key -> {
            OFFERS.remove(key);
        });
    }

    private static OfferKey key(UUID id, String hash) {
        return new OfferKey(id, hash == null ? "" : hash.toLowerCase(Locale.ROOT));
    }
}
