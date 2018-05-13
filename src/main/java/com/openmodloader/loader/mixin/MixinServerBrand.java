package com.openmodloader.loader.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net.minecraft.server.MinecraftServer")
public abstract class MixinServerBrand {

    /**
     * @author Coded
     */
    @Overwrite
    public String getServerModName() {
        return "OpenModLoader";
    }


}
