package com.openmodloader.network;

import net.fabricmc.api.Side;
import net.minecraft.util.PacketByteBuf;

public interface IPacket {

    void write(PacketByteBuf byteBuf);

    void read(PacketByteBuf byteBuf);

    void handle(Side side);

    default boolean handleOnMainThread() {
        return true;
    }
}
