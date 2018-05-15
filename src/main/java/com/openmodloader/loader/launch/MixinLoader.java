package com.openmodloader.loader.launch;

import com.openmodloader.loader.ModInfo;
import net.fabricmc.api.Side;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

public class MixinLoader {

	public static Map<String, ModInfo> mods = new HashMap<>();
	protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");


	public static void initMixins(Side side, File modsDir) throws IOException {

        //TODO at this point read all of the pre appied mixins, and prevent them from being loaded

        MixinBootstrap.init();

	    findMods(modsDir);
	    mods.values().forEach(modInfo -> {
		    Arrays.stream(modInfo.getMixins()).forEach(Mixins::addConfiguration);
		    if(side == Side.CLIENT){
			    Arrays.stream(modInfo.getClientMixins()).forEach(Mixins::addConfiguration);
		    } else {
			    Arrays.stream(modInfo.getServerMixins()).forEach(Mixins::addConfiguration);
		    }
	    });
    }

    //This has to be done seperate from the main mod loading as it is on a different class path
	private static List<ModInfo> locateClasspathMods(File modsDir) {
		List<ModInfo> mods = new ArrayList<>();
		String javaHome = System.getProperty("java.home");

		URL[] urls = Launch.classLoader.getURLs();
		for (URL url : urls) {
			if (url.getPath().startsWith(javaHome) || url.getPath().startsWith(modsDir.getAbsolutePath())) {
				continue;
			}
			LOGGER.debug("Attempting to find classpath mods from " + url.getPath());

			File f = new File(url.getFile());
			if (f.exists()) {
				if (f.isDirectory()) {
					File modJson = new File(f, "mod.json");
					if (modJson.exists()) {
						try {
							mods.addAll(Arrays.asList(ModInfo.readFromFile(modJson)));
						} catch (FileNotFoundException e) {
							LOGGER.error("Unable to load mod from directory " + f.getPath());
							e.printStackTrace();
						}
					}
				}
			}
		}
		return mods;
	}

	private static void findMods( File modsDir) throws IOException {
		for (ModInfo info : locateClasspathMods(modsDir)) {
			mods.put(info.getModId(), info);
		}
		for (File file : FileUtils.listFiles(modsDir, new String[]{"jar"}, true)) {
			JarFile jar = new JarFile(file);
			ModInfo[] infos = ModInfo.readFromJar(jar);
			if (infos == null)
				continue;
			for (ModInfo info : infos) {
				mods.put(info.getModId(), info);
			}
		}
	}


}
