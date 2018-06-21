package com.openmodloader.network.mixin;

import com.openmodloader.network.NetworkManager;
import net.fabricmc.api.Side;
import net.minecraft.client.network.handler.NetworkGameHandlerClient;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkGameHandlerClient.class, remap = false)
public abstract class CPacketHandler {

    @Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/client/CPacketCustomPayload;)V", at = @At("RETURN"))
    public void onCustomPayload(CPacketCustomPayload packet, CallbackInfo info) {
        if (packet.getChannel().equals(NetworkManager.CHANNEL)) {
            PacketByteBuf buf = packet.getData();
            NetworkManager.handleIncomingPacket(buf, Side.CLIENT);
        }
    }
}