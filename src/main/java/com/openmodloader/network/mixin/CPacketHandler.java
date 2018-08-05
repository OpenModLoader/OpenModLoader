package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import com.openmodloader.network.NetworkManager;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.fabricmc.api.Side;
import net.minecraft.client.network.handler.NetworkGameHandlerClient;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;

@Mixin(value = NetworkGameHandlerClient.class)
public abstract class CPacketHandler {

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void onCustomPayload(CPacketCustomPayload packet) {
	    IPacketData packetData = (IPacketData) packet;
        if (packetData.getChannel().equals(NetworkManager.CHANNEL)) {
            PacketByteBuf buf = packetData.getData();
            NetworkManager.handleIncomingPacket(buf, Side.CLIENT);
        }
    }
}