package com.openmodloader.api.mod;

import com.github.zafarkhaja.semver.Version;

public class ModAnnotationData implements IModData {
    private final String modid;
    private final Version version;
    private final String[] deps;

    public ModAnnotationData(Class<?> mClass) {
        Mod annotation= mClass.getAnnotation(Mod.class);
        modid=annotation.id();
        version=Version.valueOf(annotation.version());
        deps=annotation.dependencies();
    }

    public static ModAnnotationData of(Class<?> modClass) {
        if (modClass.isAnnotationPresent(Mod.class)) {
            throw new IllegalArgumentException("Mod not present on " + modClass);
        }
        return new ModAnnotationData(modClass);
    }

    @Override
    public String getModId() {
        return modid;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String[] getDependencies() {
        return deps;
    }
}
