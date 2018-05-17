package com.openmodloader.loader.client;

import com.openmodloader.api.loader.SideHandler;
import net.fabricmc.api.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class ClientSideHandler implements SideHandler {
    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

	@Override
	public void runOnMainThread(Runnable runnable) {
		if (Minecraft.getInstance().isMainThread()) {
			runnable.run();
		} else {
			Minecraft.getInstance().scheduleOnMainThread(runnable);
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public MinecraftServer getServer() {
		return Minecraft.getInstance().getServer();
	}
}
