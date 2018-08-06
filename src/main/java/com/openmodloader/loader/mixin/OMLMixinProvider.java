package com.openmodloader.loader.mixin;

import com.openmodloader.loader.mixin.registry.*;
import me.modmuss50.fusion.api.IMixinProvider;

public class OMLMixinProvider implements IMixinProvider {
	@Override
	public Class[] getMixins(IMixinEnvironment environment) {
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
