package com.localpack.bypass.mixin;

import com.localpack.bypass.LocalPackBypassHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.server.packs.repository.PackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerMixin {

    private static final Logger LOGGER = LoggerFactory.getLogger("localpack-bypass");

    @Shadow
    public abstract void send(Packet<?> packet);

    @Inject(method = "handleResourcePackPush", at = @At("HEAD"), cancellable = true)
    private void localpackBypass$onResourcePackPush(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
        File matched = LocalPackBypassHelper.findLocalPackByHash(packet.hash());
        if (matched == null) {
            return;
        }

        LOGGER.info("Знайдено локальний ресурспак з відповідним хешем: {}", matched.getName());

        Minecraft client = Minecraft.getInstance();
        PackRepository repository = client.getResourcePackRepository();
        String packId = "file/" + matched.getName();

        Set<String> selected = new LinkedHashSet<>(repository.getSelectedIds());
        selected.add(packId);
        repository.setSelected(selected);

        this.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
        ci.cancel();
    }
}
