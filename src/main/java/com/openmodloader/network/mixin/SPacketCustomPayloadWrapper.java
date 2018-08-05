package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;


@Mixin(value = SPacketCustomPayload.class)
public class SPacketCustomPayloadWrapper implements IPacketData {

	private Identifier channel;
	private PacketByteBuf data;

	@Inject
	@Override
	public Identifier getChannel() {
		return channel;
	}

	@Inject
	@Override
	public PacketByteBuf getData() {
		return data;
	}
}
