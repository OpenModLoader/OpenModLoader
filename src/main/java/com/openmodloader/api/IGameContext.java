package com.openmodloader.api;

import net.fabricmc.api.Side;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public interface IGameContext {
    File getRunDirectory();

    @Nullable
    MinecraftServer getServer();

    Side getPhysicalSide();
}
