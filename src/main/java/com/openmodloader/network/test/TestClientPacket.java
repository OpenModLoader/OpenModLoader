package com.openmodloader.network.test;

import com.openmodloader.network.IPacket;
import net.fabricmc.api.Side;
import net.minecraft.util.PacketByteBuf;

public class TestClientPacket implements IPacket {

	String name;

	public TestClientPacket(String name) {
		this.name = name;
	}

	public TestClientPacket() {
	}

	@Override
	public void write(PacketByteBuf byteBuf) {
		byteBuf.writeInt(name.length());
		byteBuf.writeString(name);
	}

	@Override
	public void read(PacketByteBuf byteBuf) {
		name = byteBuf.readString(byteBuf.readInt());
	}

	@Override
	public void handle(Side side) {
		System.out.println("Hello from the server, the client sent: " + name);
	}
}
