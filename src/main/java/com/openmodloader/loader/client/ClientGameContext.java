package com.openmodloader.loader.client;

import com.openmodloader.api.IGameContext;
import net.fabricmc.api.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;
import java.io.File;

public class ClientGameContext implements IGameContext {
    @Override
    public File getRunDirectory() {
        return Minecraft.getInstance().runDirectory;
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return Minecraft.getInstance().getServer();
    }

    @Override
    public Side getPhysicalSide() {
        return Side.CLIENT;
    }
}
