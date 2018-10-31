package com.openmodloader.loader.launch;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import javassist.LoaderClassPath;
import me.modmuss50.fusion.transformer.MixinTransformer;
import net.minecraft.client.main.Main;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
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

	protected static void addDirectoriesFromClasspath(List<Path> paths) {
		Arrays.stream(getClassPathURLs()).filter(u->{
			try {
				return u.getProtocol().equals("file") && new File(u.toURI()).isDirectory();
			} catch (URISyntaxException e) {
				return false;
			}
		}).forEach(v->{
			try {
				Path p = Paths.get(v.toURI());
				if (!paths.contains(p))
					paths.add(p);
			} catch (URISyntaxException e){

			}
		});
	}

	protected static URL[] getClassPathURLs() {
		URL[] urls;
		if (OMLLaunchHandler.class.getClassLoader() instanceof URLClassLoader){
			urls = ((URLClassLoader) OMLLaunchHandler.class.getClassLoader()).getURLs();
		} else {
			String classPath = appendPath(System.getProperty("java.class.path"), System.getProperty("env.class.path"));
			urls = pathToURLs(classPath);
		}
		return urls;
	}

	private static URL fileToURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			LOGGER.error("Could not convert {} to URL", file.toString(), e);
			return null;
		}
	}

	//the following 2 methods are largely copied from internal sun.* classes
	private static String appendPath(String pathTo, String pathFrom) {
		if (pathTo == null || pathTo.length() == 0) {
			return pathFrom;
		} else if (pathFrom == null || pathFrom.length() == 0) {
			return pathTo;
		} else {
			return pathTo  + File.pathSeparator + pathFrom;
		}
	}

	private static URL[] pathToURLs(String path) {
		StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
		URL[] urls = new URL[st.countTokens()];
		int count = 0;
		while (st.hasMoreTokens()) {
			URL url = fileToURL(new File(st.nextToken()));
			if (url != null) {
				urls[count++] = url;
			}
		}
		if (urls.length != count) {
			URL[] tmp = new URL[count];
			System.arraycopy(urls, 0, tmp, 0, count);
			urls = tmp;
		}
		return urls;
	}
}
