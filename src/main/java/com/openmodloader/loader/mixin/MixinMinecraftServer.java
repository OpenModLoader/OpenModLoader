package com.openmodloader.loader.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    /**
     * @reason Change modification branding
     * @author Coded
     */
    @Overwrite(remap = false)
    public String getServerModName() {
        return "OpenModLoader";
    }


}
