package com.openmodloader.loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModInfo {

    private String modid;
    private String name;
    private String version;
    private String mcversion;
    private String side = "";
    private String mainClass = "";
    private boolean library = false;
    private String languageAdapter = "com.openmodloader.loader.language.JavaLanguageAdapter";
    private String [] mixins = new String[0];
	private String [] dependencies = new String[0];
	private String [] libraries = new String[0];
	private File origin;

	public ModInfo(String modid) {
        this.modid = modid;
    }

    public ModInfo() {
    }

    public File getOrigin() {
        return origin;
    }

    public ModInfo setOrigin(File origin) {
        this.origin = origin;
        return this;
    }

    @Nullable
    public static ModInfo[] readFromJar(@Nonnull JarFile file) throws IOException {
        ZipEntry entry = file.getEntry("mod.json");
        if (entry == null)
            return null;
        return OpenModLoader.getGson().fromJson(new InputStreamReader(file.getInputStream(entry)), ModInfo[].class);
    }

    public static ModInfo[] readFromFile(File modJson) throws FileNotFoundException {
        return OpenModLoader.getGson().fromJson(new InputStreamReader(new FileInputStream(modJson)), ModInfo[].class);
    }

    public boolean isLibrary() {
        return library;
    }

    public String getModId() {
        return modid;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMinecraftVersion() {
        return mcversion;
    }

    public String getSide() {
        return side;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getLanguageAdapter() {
        return languageAdapter;
    }

	public String[] getMixins() {
		return mixins;
	}

    public String[] getDependencies() {
        return dependencies;
    }

    public String[] getLibraries() {
        return libraries;
    }
}