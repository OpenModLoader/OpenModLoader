package com.openmodloader.loader;

import net.fabricmc.api.Side;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public interface OMLContext {
    File getRunDirectory();

    @Nullable
    MinecraftServer getServer();

    Side getPhysicalSide();
}
