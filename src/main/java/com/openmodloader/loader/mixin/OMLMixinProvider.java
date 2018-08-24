package com.openmodloader.loader.mixin;

import com.openmodloader.loader.mixin.registry.*;
import me.modmuss50.fusion.api.IMixinProvider;

import java.util.Map;

public class OMLMixinProvider implements IMixinProvider {
    @Override
    public Class[] getMixins(Map<String, String> environmentData) {
        return new Class[]{
                MixinClientBrand.class,
                MixinCommandManager.class,
                MixinDedicatedServer.class,
                MixinGuiScreen.class,
                MixinMinecraftClient.class,
                MixinMinecraftServer.class,
                //Registry
                MixinBiome.class,
                MixinBlock.class,
                MixinEnchantment.class,
                MixinItem.class,
                MixinPotion.class
        };
    }
}
