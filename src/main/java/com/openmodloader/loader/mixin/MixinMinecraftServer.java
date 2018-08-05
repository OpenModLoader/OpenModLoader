package com.openmodloader.loader.mixin;

import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import net.minecraft.server.MinecraftServer;


@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    /**
     * @reason Change modification branding
     * @author Coded
     */
    @Inject
    public String getServerModName() {
        return "OpenModLoader";
    }


}
