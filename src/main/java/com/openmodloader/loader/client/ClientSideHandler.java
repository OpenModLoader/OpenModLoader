package com.openmodloader.loader.client;

import com.openmodloader.api.loader.SideHandler;
import net.fabricmc.api.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class ClientSideHandler implements SideHandler {
    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

	@Override
	public void runOnMainThread(Runnable runnable) {
		if (MinecraftClient.getInstance().isMainThread()) {
			runnable.run();
		} else {
			MinecraftClient.getInstance().scheduleOnMainThread(runnable);
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return MinecraftClient.getInstance().player;
	}

	@Override
	public MinecraftServer getServer() {
		return MinecraftClient.getInstance().getServer();
	}
}
