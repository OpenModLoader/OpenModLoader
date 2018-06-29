package com.openmodloader.network;

import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public interface IPacketData {
	Identifier getChannel();

	PacketByteBuf getData();
}
