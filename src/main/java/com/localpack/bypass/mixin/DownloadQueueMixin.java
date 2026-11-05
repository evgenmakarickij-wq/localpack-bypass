package com.localpack.bypass.mixin;

import com.google.common.hash.HashCode;
import com.localpack.bypass.Config;
import com.localpack.bypass.PackCache;
import com.localpack.bypass.PackTracker;
import dev.mcchopik.modulib.net.ParallelFileDownloader;
import net.minecraft.server.packs.DownloadQueue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(DownloadQueue.class)
public abstract class DownloadQueueMixin {

    @Shadow
    @Final
    private Path cacheDir;

    /** Replaces only the byte-transfer implementation; vanilla still owns protocol state and reloads. */
    @Inject(method = "runDownload", at = @At("HEAD"), cancellable = true)
    private void localpackBypass$downloadBatch(DownloadQueue.BatchConfig batchConfig,
                                               Map<UUID, DownloadQueue.DownloadRequest> requests,
                                               CallbackInfoReturnable<DownloadQueue.BatchResult> cir) {
        long generation = com.localpack.bypass.PersistentPacks.beginTransfer();
        if (generation < 0) {
            DownloadQueue.BatchResult denied = new DownloadQueue.BatchResult();
            denied.failed().addAll(requests.keySet());
            cir.setReturnValue(denied);
            return;
        }
        try {
        Set<UUID> seeded = new HashSet<>();
        List<ParallelFileDownloader.Request<UUID>> batch = new ArrayList<>(requests.size());
        requests.forEach((id, request) -> {
            HashCode hash = request.hash();
            String expected = hash == null ? null : hash.toString();
            Path targetDirectory = cacheDir.resolve(id.toString());
            if (expected != null && PackCache.seed(targetDirectory.resolve(expected), expected)) {
                seeded.add(id);
            }
            batch.add(new ParallelFileDownloader.Request<>(id, request.url(), expected, targetDirectory));
        });
        synchronized (batchConfig.listener()) {
            requests.forEach((id, request) -> batchConfig.listener().requestStart());
            batchConfig.listener().downloadStart(OptionalLong.empty());
        }

        Config config = Config.get();
        ParallelFileDownloader.Options options = new ParallelFileDownloader.Options(
                config.parallelDownloads(),
                batchConfig.maxSize(),
                config.downloadSpeedMiB() * 1024L * 1024L,
                15_000,
                config.downloadTimeoutSeconds() * 1_000,
                config.downloadAttempts(),
                true,
                batchConfig.proxy(),
                batchConfig.headers());

        ParallelFileDownloader.BatchResult<UUID> downloaded;
        Map<UUID, Long> progress = new ConcurrentHashMap<>();
        try {
            downloaded = ParallelFileDownloader.download(batch, options,
                    new ParallelFileDownloader.Listener<>() {
                        @Override
                        public void started(UUID id, long expectedBytes) {
                        }

                        @Override
                        public void progressed(UUID id, long downloadedBytes) {
                            progress.put(id, downloadedBytes);
                            synchronized (batchConfig.listener()) {
                                batchConfig.listener().downloadedBytes(
                                        progress.values().stream().mapToLong(Long::longValue).sum());
                            }
                        }

                        @Override
                        public void finished(UUID id, boolean success) {
                        }
                    });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            downloaded = new ParallelFileDownloader.BatchResult<>(Map.of(),
                    requests.keySet().stream().collect(java.util.stream.Collectors.toMap(id -> id, id -> e)));
        }

        DownloadQueue.BatchResult result = new DownloadQueue.BatchResult();
        result.downloaded().putAll(downloaded.downloaded());
        result.failed().addAll(downloaded.failed().keySet());
        downloaded.downloaded().forEach((id, path) -> {
            DownloadQueue.DownloadRequest request = requests.get(id);
            String hash = request == null || request.hash() == null ? "" : request.hash().toString();
            PackTracker.completed(id, path, hash, seeded.contains(id), generation);
        });
        downloaded.failed().keySet().forEach(id -> {
            DownloadQueue.DownloadRequest request = requests.get(id);
            String hash = request == null || request.hash() == null ? "" : request.hash().toString();
            PackTracker.failed(id, hash, generation);
        });
        synchronized (batchConfig.listener()) {
            batchConfig.listener().requestFinished(result.failed().isEmpty());
        }
        cir.setReturnValue(result);
        } finally {
            com.localpack.bypass.PersistentPacks.endTransfer();
        }
    }
}
