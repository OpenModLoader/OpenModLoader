package com.openmodloader.network.mixin;

import com.openmodloader.network.IPacketData;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SPacketCustomPayload.class)
public class SPacketCustomPayloadWrapper implements IPacketData {

	@Shadow
	private Identifier channel;
	@Shadow
	private PacketByteBuf data;

	@Override
	public Identifier getChannel() {
		return channel;
	}

	@Override
	public PacketByteBuf getData() {
		return data;
	}
}
