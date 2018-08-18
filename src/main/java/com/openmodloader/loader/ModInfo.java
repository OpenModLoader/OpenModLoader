package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import net.fabricmc.api.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModInfo {

    private String modid;
    private String name;
    private Version version;
    private Version mcversion;
    private Side side = Side.UNIVERSAL;
    private String mainClass = "";
    private String icon = "";
    private int assetVersion = 4;
    private boolean library = false;
    private String languageAdapter = "com.openmodloader.loader.language.JavaLanguageAdapter";
    private String[] dependencies = new String[0];
    private String[] libraries = new String[0];
    private File origin;

    public ModInfo(String modid) {
        this.modid = modid;
    }

    public ModInfo() {
    }

    public Version getVersion() {
        return version;
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

    public File getOrigin() {
        return origin;
    }

    public ModInfo setOrigin(File origin) {
        this.origin = origin;
        return this;
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

    public String getIcon() {
        return icon.isEmpty() ? modid + ".png" : icon;
    }

    public Version getMinecraftVersion() {
        return mcversion;
    }

    public int getAssetVersion() {
        return assetVersion;
    }

    public Side getSide() {
        return side;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getLanguageAdapter() {
        return languageAdapter;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String[] getLibraries() {
        return libraries;
    }
}