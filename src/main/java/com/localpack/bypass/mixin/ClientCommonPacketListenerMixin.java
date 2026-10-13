package com.localpack.bypass.mixin;

import com.localpack.bypass.LocalPackBypassHelper;
import com.localpack.bypass.Disconnector;
import com.localpack.bypass.PackFlow;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerMixin implements Disconnector {

    @Shadow
    @Final
    protected Connection connection;

    @Override
    public void localpackBypass$disconnect(Component reason) {
        this.connection.disconnect(reason);
    }

    @Override
    public Object localpackBypass$connectionKey() {
        return this.connection;
    }

    @Inject(method = "handleResourcePackPush", at = @At("HEAD"), cancellable = true)
    private void localpackBypass$onResourcePackPush(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
        if (!LocalPackBypassHelper.isValidSha1(packet.hash())) {
            return;
        }

        PackFlow.handle((ClientCommonPacketListenerImpl) (Object) this, packet);
        ci.cancel();
    }
}
