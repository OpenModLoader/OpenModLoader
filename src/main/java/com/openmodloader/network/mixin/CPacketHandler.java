package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.client.network.handler.NetworkGameHandlerClient;
import net.minecraft.network.packet.client.CPacketCustomPayload;

@Mixin(value = NetworkGameHandlerClient.class)
public abstract class CPacketHandler {

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void onCustomPayload(CPacketCustomPayload packet) {
	    IPacketData packetData = (IPacketData) packet;
        // TODO
    }
}
