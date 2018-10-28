package com.openmodloader.loader.server;

import com.openmodloader.api.IOmlContext;
import net.fabricmc.api.Side;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public class ServerOmlContext implements IOmlContext {
    private final MinecraftServer server;

    public ServerOmlContext(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public File getRunDirectory() {
        return this.server.getRunDirectory();
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return this.server;
    }

    @Override
    public Side getPhysicalSide() {
        return Side.SERVER;
    }
}
