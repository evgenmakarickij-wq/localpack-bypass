package com.localpack.bypass.mixin;

import com.localpack.bypass.PackTracker;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    private void localpackBypass$onSend(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ServerboundResourcePackPacket response
                && response.action() == ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED) {
            PackTracker.successful(response.id());
        }
    }
}
