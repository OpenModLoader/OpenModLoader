package com.openmodloader.loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModInfo {

    private String modid;
    private String name;
    private String version;
    private String mcversion;
    private String languageAdapter = "com.openmodloader.loader.language.JavaLanguageAdapter";

    public ModInfo(String modid) {
        this.modid = modid;
    }

    public ModInfo() {
    }

    @Nullable
    public static ModInfo readFromJar(@Nonnull JarFile file) throws IOException {
        ZipEntry entry = file.getEntry("mod.json");
        if (entry == null)
            return null;
        return OpenModLoader.getGson().fromJson(new InputStreamReader(file.getInputStream(entry)), ModInfo.class);
    }

    public String getModid() {
        return modid;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMcversion() {
        return mcversion;
    }
}