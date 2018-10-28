package com.openmodloader.loader.launch;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import javassist.LoaderClassPath;
import me.modmuss50.fusion.transformer.MixinTransformer;
import net.minecraft.client.main.Main;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class OMLLaunchHandler implements ILaunchHandlerService {

	protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoaderLauncher");

	private Class[] transformTargets = new Class[]{
			OMLLaunchHandler.class,
			MinecraftServer.class
	};

	@Override
	public String name() {
		return "oml";
	}

	@Override
	public Path[] identifyTransformationTargets() {
		return Arrays.stream(transformTargets).map(aClass -> {
			try {
				return Paths.get(aClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).toArray(Path[]::new);
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

	public String getMainClass() {
		return "net.minecraft.client.main.Main";
	}

}
