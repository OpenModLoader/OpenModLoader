package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;


@Mixin(value = CPacketCustomPayload.class)
public class CPacketCustomPayloadWrapper implements IPacketData {

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