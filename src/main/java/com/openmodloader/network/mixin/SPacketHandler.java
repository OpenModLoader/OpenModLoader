package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.network.handler.NetworkGameHandlerServer;
import net.minecraft.network.packet.server.SPacketCustomPayload;

@Mixin(value = NetworkGameHandlerServer.class)
public abstract class SPacketHandler {

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void onCustomPayload(SPacketCustomPayload packet) {
	    IPacketData packetData = (IPacketData) packet;
        // TODO
    }
}
