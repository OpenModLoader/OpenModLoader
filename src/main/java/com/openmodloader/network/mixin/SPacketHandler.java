package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import com.openmodloader.network.NetworkManager;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.fabricmc.api.Side;
import net.minecraft.network.handler.NetworkGameHandlerServer;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;

@Mixin(value = NetworkGameHandlerServer.class)
public abstract class SPacketHandler {

    @Rewrite(behavior = Rewrite.Behavior.END)
    public void onCustomPayload(SPacketCustomPayload packet) {
        IPacketData packetData = (IPacketData) packet;
        if (packetData.getChannel().equals(NetworkManager.CHANNEL)) {
            PacketByteBuf buf = packetData.getData();
            NetworkManager.handleIncomingPacket(buf, Side.SERVER);
        }
    }
}