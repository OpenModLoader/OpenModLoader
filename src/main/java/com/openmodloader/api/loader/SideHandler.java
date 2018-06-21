package com.openmodloader.api.loader;

import net.fabricmc.api.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public interface SideHandler {
    Side getSide();

    void runOnMainThread(Runnable runnable);

    EntityPlayer getClientPlayer();

    MinecraftServer getServer();
}
