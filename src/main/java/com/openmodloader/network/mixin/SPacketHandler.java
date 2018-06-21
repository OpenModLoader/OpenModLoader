package com.openmodloader.network.mixin;

import com.openmodloader.network.NetworkManager;
import net.fabricmc.api.Side;
import net.minecraft.network.handler.NetworkGameHandlerServer;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkGameHandlerServer.class, remap = false)
public abstract class SPacketHandler {

    @Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/server/SPacketCustomPayload;)V", at = @At("RETURN"))
    public void onCustomPayload(SPacketCustomPayload packet, CallbackInfo info) {
        if (packet.getChannel().equals(NetworkManager.CHANNEL)) {
            PacketByteBuf buf = packet.getData();
            NetworkManager.handleIncomingPacket(buf, Side.SERVER);
        }
    }
}