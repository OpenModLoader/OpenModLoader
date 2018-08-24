package com.openmodloader.network.test;

import com.openmodloader.api.event.Subscribe;
import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.event.GuiEvent;
import com.openmodloader.network.NetworkManager;
import net.fabricmc.api.Side;
import net.fabricmc.api.Sided;
import net.minecraft.util.Identifier;

public class TestPackets {

    public static void load() {
        NetworkManager.registerPacket(new Identifier("openmodloader", "testpacket"), TestClientPacket.class);
        OpenModLoader.EVENT_BUS.register(new TestPackets());
    }

    @Sided(Side.CLIENT)
    @Subscribe
    public void openGui(GuiEvent.Open event) {
        if (event.getClient().player != null) {
            NetworkManager.sendToServer(new TestClientPacket(event.getGui().getClass().getCanonicalName()));
        }

    }

}
