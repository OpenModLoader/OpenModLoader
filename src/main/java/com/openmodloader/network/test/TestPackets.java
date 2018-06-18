package com.openmodloader.network.test;

import com.openmodloader.api.event.Event;
import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.event.GuiEvent;
import com.openmodloader.network.NetworkManager;
import net.fabricmc.api.Side;
import net.fabricmc.api.Sided;

public class TestPackets {

	public static void load() {
		NetworkManager.registerPacket(TestClientPacket.class);
		OpenModLoader.EVENT_BUS.register(new TestPackets());
	}

	@Sided(Side.CLIENT)
	@Event.Subscribe
	public void openGui(GuiEvent.Open event) {
		if (event.getClient().player != null) {
			NetworkManager.sendToServer(new TestClientPacket(event.getGui().getClass().getCanonicalName()));
		}

	}

}
