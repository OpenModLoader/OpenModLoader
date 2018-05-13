package com.openmodloader.loader.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OpenTweaker implements ITweaker {
    protected Map<String, String> args;

    @Override
    public void acceptOptions(List<String> localArgs, File gameDir, File assetsDir, String profile) {
        this.args = (Map<String, String>) Launch.blackboard.get("launchArgs");
        if (this.args == null) {
            this.args = new HashMap<>();
            Launch.blackboard.put("launchArgs", this.args);
        }

        for (int i = 0; i < localArgs.size(); i++) {
            String arg = localArgs.get(i);
            if (arg.startsWith("--")) {
                this.args.put(arg, localArgs.get(i + 1));
                i++;
            }
        }

        if (!this.args.containsKey("--version")) {
            this.args.put("--version", profile != null ? profile : "OpenModLoader");
        }

        if (!this.args.containsKey("--gameDir")) {
            if (gameDir == null)
                gameDir = new File(".");
            this.args.put("--gameDir", gameDir.getAbsolutePath());
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        //TODO: Replace with mixins
        if (Boolean.parseBoolean(System.getProperty("oml.development", "false"))) {
            launchClassLoader.registerTransformer("com.openmodloader.loader.transformer.AccessTransformer");
        }
        launchClassLoader.registerTransformer("com.openmodloader.loader.transformer.BrandTransformer");
        launchClassLoader.registerTransformer("com.openmodloader.loader.transformer.GameTransformer");
    }

    @Override
    public String[] getLaunchArguments() {
        List<String> launchArgs = new ArrayList<>();
        for (Map.Entry<String, String> arg : this.args.entrySet()) {
            launchArgs.add(arg.getKey());
            launchArgs.add(arg.getValue());
        }
        return launchArgs.toArray(new String[launchArgs.size()]);
    }
}
