package com.openmodloader.loader.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import com.openmodloader.loader.LoaderBootstrap;
import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.server.ServerGameContext;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.command.CommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.UserCache;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;

@Mixin(value = DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    public MixinDedicatedServer(@Nullable File aFile1, Proxy aProxy2, DataFixer aDataFixer3, CommandManager aCommandManager4, YggdrasilAuthenticationService aYggdrasilAuthenticationService5, MinecraftSessionService aMinecraftSessionService6, GameProfileRepository aGameProfileRepository7, UserCache aUserCache8) {
        super(aFile1, aProxy2, aDataFixer3, aCommandManager4, aYggdrasilAuthenticationService5, aMinecraftSessionService6, aGameProfileRepository7, aUserCache8);
    }

    @Rewrite(target = "setupServer()Z", behavior = Rewrite.Behavior.START)
    public void setupServer_() throws IOException {
        OpenModLoader.offerContext(new ServerGameContext(this));

        LoaderBootstrap bootstrap = new LoaderBootstrap();
        OpenModLoader oml = bootstrap.create();
        oml.initialize();
    }
}
