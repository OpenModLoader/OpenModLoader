package com.openmodloader.loader.launch;

import net.fabricmc.api.Side;

public class OpenServerTweaker extends OpenTweaker {

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.server.MinecraftServer";
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }
}
