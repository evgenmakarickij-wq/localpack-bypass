package com.localpack.bypass.mixin;

import com.localpack.bypass.PersistentPacks;
import net.minecraft.client.resources.server.DownloadedPackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadedPackSource.class)
public abstract class DownloadedPackSourceMixin {

    @Inject(method = "cleanupAfterDisconnect", at = @At("TAIL"))
    private void localpackBypass$afterDisconnect(CallbackInfo ci) {
        PersistentPacks.afterDisconnect();
    }
}
