package com.openmodloader.loader.launch;

import net.minecraft.client.main.Main;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class OMLClientLaunchHandler extends OMLLaunchHandler {

	private Class[] transformTargets = new Class[]{
			OMLLaunchHandler.class,
			Main.class
	};

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
	public String name() {
		return "omlclient";
	}

	@Override
	public String getMainClass() {
		return "net.minecraft.client.main.Main";
	}
}
