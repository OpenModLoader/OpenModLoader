package com.openmodloader.loader.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.server.ServerSideHandler;
import net.minecraft.command.CommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.UserCache;
import net.minecraft.util.datafix.Datafix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;

@Mixin(value = DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    public MixinDedicatedServer(@Nullable File aFile1, Proxy aProxy2, Datafix aDatafix3, CommandManager aCommandManager4, YggdrasilAuthenticationService aYggdrasilAuthenticationService5, MinecraftSessionService aMinecraftSessionService6, GameProfileRepository aGameProfileRepository7, UserCache aUserCache8) {
        super(aFile1, aProxy2, aDatafix3, aCommandManager4, aYggdrasilAuthenticationService5, aMinecraftSessionService6, aGameProfileRepository7, aUserCache8);
    }

    @Inject(method = "setupServer", at = @At("HEAD"))
    public void setupServer(CallbackInfoReturnable<Boolean> info) throws IOException {
        OpenModLoader.initialize(this.getFile(""), new ServerSideHandler(this));
    }


}
