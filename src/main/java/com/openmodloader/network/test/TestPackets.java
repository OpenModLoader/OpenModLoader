package com.openmodloader.network.test;

import com.openmodloader.network.NetworkManager;
import net.minecraft.util.Identifier;

public class TestPackets {

    public static void load() {
        NetworkManager.registerPacket(new Identifier("openmodloader", "testpacket"), TestClientPacket.class);
    }
}
