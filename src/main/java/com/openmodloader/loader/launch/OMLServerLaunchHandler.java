package com.openmodloader.loader.launch;

public class OMLServerLaunchHandler extends OMLLaunchHandler {

	@Override
	public String name() {
		return "omlserver";
	}

	@Override
	public String getMainClass() {
		return "net.minecraft.server.MinecraftServer";
	}
}
