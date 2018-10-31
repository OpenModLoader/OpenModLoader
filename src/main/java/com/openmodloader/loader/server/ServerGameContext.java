package com.openmodloader.loader.server;

import com.openmodloader.api.IGameContext;
import net.fabricmc.api.Side;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public class ServerGameContext implements IGameContext {
    private final MinecraftServer server;

    public ServerGameContext(MinecraftServer server) {
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
