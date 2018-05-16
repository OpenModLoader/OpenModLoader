package com.openmodloader.loader.server;

import com.openmodloader.api.loader.SideHandler;
import net.fabricmc.api.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class ServerSideHandler implements SideHandler {

	private MinecraftServer minecraftServer;

	public ServerSideHandler(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
	}

	@Override
	public Side getSide() {
		return Side.SERVER;
	}

	@Override
	public void runOnMainThread(Runnable runnable) {
		if (minecraftServer.isMainThread()) {
			runnable.run();
		} else {
			minecraftServer.scheduleOnMainThread(runnable);
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

	@Override
	public MinecraftServer getServer() {
		return minecraftServer;
	}
}
