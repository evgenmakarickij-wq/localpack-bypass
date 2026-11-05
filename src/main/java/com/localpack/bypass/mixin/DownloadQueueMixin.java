package com.localpack.bypass.mixin;

import com.google.common.hash.HashCode;
import com.localpack.bypass.PackCache;
import com.localpack.bypass.PackTracker;
import net.minecraft.server.packs.DownloadQueue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mixin(DownloadQueue.class)
public abstract class DownloadQueueMixin {

    @Shadow
    @Final
    private Path cacheDir;

    @Unique
    private final ThreadLocal<Set<UUID>> localpackBypass$seeded = ThreadLocal.withInitial(HashSet::new);

    @Inject(method = "runDownload", at = @At("HEAD"))
    private void localpackBypass$seedCache(DownloadQueue.BatchConfig config,
                                           Map<UUID, DownloadQueue.DownloadRequest> requests,
                                           CallbackInfoReturnable<DownloadQueue.BatchResult> cir) {
        Set<UUID> seeded = localpackBypass$seeded.get();
        seeded.clear();
        requests.forEach((id, request) -> {
            HashCode hash = request.hash();
            if (hash == null) return;
            Path target = cacheDir.resolve(id.toString()).resolve(hash.toString());
            if (PackCache.seed(target, hash.toString())) seeded.add(id);
        });
    }

    @Inject(method = "runDownload", at = @At("RETURN"))
    private void localpackBypass$archive(DownloadQueue.BatchConfig config,
                                         Map<UUID, DownloadQueue.DownloadRequest> requests,
                                         CallbackInfoReturnable<DownloadQueue.BatchResult> cir) {
        Set<UUID> seeded = localpackBypass$seeded.get();
        DownloadQueue.BatchResult result = cir.getReturnValue();
        result.downloaded().forEach((id, path) -> PackTracker.completed(id, path, seeded.contains(id)));
        result.failed().forEach(PackTracker::failed);
        seeded.clear();
        localpackBypass$seeded.remove();
    }
}
