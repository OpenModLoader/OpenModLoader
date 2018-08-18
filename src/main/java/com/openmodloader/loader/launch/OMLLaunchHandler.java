package com.openmodloader.loader.launch;

import com.openmodloader.loader.ModInfo;
import com.openmodloader.loader.OpenModLoader;
import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import javassist.LoaderClassPath;
import me.modmuss50.fusion.transformer.MixinTransformer;
import net.minecraft.client.main.Main;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class OMLLaunchHandler implements ILaunchHandlerService {

	protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoaderLauncher");

	private Class[] transformTargets = new Class[] {
		OMLLaunchHandler.class,
		Main.class
	};

	@Override
	public String name() {
		return "oml";
	}
	private static List<ModInfo> locateClasspathMods() {
		List<ModInfo> mods = new ArrayList<>();
		String javaHome = System.getProperty("java.home");


		return mods;
	}
	@Override
	public Path[] identifyTransformationTargets() {
		ClassLoader loader = OMLLaunchHandler.class.getClassLoader();
		String javaHome = System.getProperty("java.home");
		Path[] paths = new Path[0];
		if (loader instanceof URLClassLoader) {
			for (URL url : ((URLClassLoader) loader).getURLs()) {
				if (url.getPath().startsWith(javaHome)) {
					continue;
				}
				LOGGER.debug("Attempting to find classpath mods from " + url.getPath());

				File f = new File(url.getFile());
				if (f.exists()) {
					if (f.isDirectory()) {
						File modJson = new File(f, "mod.json");
						if (modJson.exists()) {
							try {
								ArrayUtils.add(paths,Paths.get(url.toURI()));
							} catch (URISyntaxException e) {
								e.printStackTrace();
							}
						}
					} else {
						if (FilenameUtils.isExtension(f.getName(), "jar")) {
							try {
								JarFile jarFile = new JarFile(f);
								if (jarFile.getEntry("mod.json") != null) {
									ArrayUtils.add(paths,Paths.get(url.toURI()));
								}
							} catch (IOException | URISyntaxException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		return ArrayUtils.addAll(paths,
		 Arrays.stream(transformTargets).map(aClass -> {
			try {
				return Paths.get(aClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).toArray(Path[]::new));
	}

	@Override
	public Callable<Void> launchService(String[] arguments, ITransformingClassLoader launchClassLoader) {
		return () -> {
			LOGGER.info("Starting modded minecraft with oml");

			MixinTransformer.cp.appendClassPath(new LoaderClassPath(launchClassLoader.getInstance()));

			//Taken from forge, moves the MC main class onto the correct class loader
			Field scl = ClassLoader.class.getDeclaredField("scl");
			scl.setAccessible(true);
			scl.set(null, launchClassLoader.getInstance());
			Thread.currentThread().setContextClassLoader(launchClassLoader.getInstance());
			Class.forName(getMainClass(), true, launchClassLoader.getInstance()).getMethod("main", String[].class).invoke(null, (Object) arguments);

			return null;
		};
	}

	public String getMainClass(){
		return "net.minecraft.client.main.Main";
	}

}
