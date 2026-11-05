package com.localpack.bypass.mixin;

import com.localpack.bypass.PackTracker;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerMixin {

    @Shadow
    @Final
    protected ServerData serverData;

    @Inject(method = "handleResourcePackPush", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift = At.Shift.AFTER))
    private void localpackBypass$onResourcePackPush(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
        PackTracker.observe(serverData, packet);
    }

    @Inject(method = "handleResourcePackPop", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V", shift = At.Shift.AFTER))
    private void localpackBypass$onResourcePackPop(ClientboundResourcePackPopPacket packet, CallbackInfo ci) {
        packet.id().ifPresentOrElse(PackTracker::forget, PackTracker::clear);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void localpackBypass$onDisconnect(CallbackInfo ci) {
        PackTracker.clear();
    }
}
