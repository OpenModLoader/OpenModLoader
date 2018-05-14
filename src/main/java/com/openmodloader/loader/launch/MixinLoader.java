package com.openmodloader.loader.launch;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class MixinLoader {

	public static void initMixins(){

		//TODO at this point read all of the pre appied mixins, and prevent them from being loaded

		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.openmodloader.json");

		//TODO load all mod mixins

	}

}
