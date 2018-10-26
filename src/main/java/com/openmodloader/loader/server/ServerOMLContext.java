package com.openmodloader.loader.server;

import com.openmodloader.loader.OMLContext;
import net.fabricmc.api.Side;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public class ServerOMLContext implements OMLContext {
    private final MinecraftServer server;

    public ServerOMLContext(MinecraftServer server) {
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
