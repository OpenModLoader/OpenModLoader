package com.openmodloader.loader.launch;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OMLDevServerLaunchHandler extends OMLServerLaunchHandler {

	@Override
	public Path[] identifyTransformationTargets() {
		Path[] basePaths = super.identifyTransformationTargets();
		List<Path> paths = new ArrayList<>(Arrays.asList(basePaths));
		addDirectoriesFromClasspath(paths);
		return paths.toArray(new Path[0]);
	}

	@Override
	public String name() {
		return "omldevserver";
	}
}
