package com.openmodloader.loader.launch;

import com.google.common.collect.Lists;

import java.nio.file.Path;
import java.util.List;

public class OMLDevServerLaunchHandler extends OMLServerLaunchHandler {

    @Override
    public Path[] identifyTransformationTargets() {
        Path[] basePaths = super.identifyTransformationTargets();
        List<Path> paths = Lists.newArrayList(basePaths);
        addDirectoriesFromClasspath(paths);
        return paths.toArray(new Path[0]);
    }

    @Override
    public String name() {
        return "omldevserver";
    }
}
