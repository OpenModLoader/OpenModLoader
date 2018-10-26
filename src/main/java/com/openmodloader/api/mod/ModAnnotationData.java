package com.openmodloader.api.mod;

public class ModAnnotationData implements IModData {
    private Mod annotation;

    private ModAnnotationData(Mod annotation) {
        this.annotation = annotation;
    }

    public static ModAnnotationData of(Class<?> modClass) {
        Mod annotation = modClass.getAnnotation(Mod.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Mod present not present on " + modClass);
        }
        return new ModAnnotationData(annotation);
    }

    @Override
    public String getModId() {
        return annotation.id();
    }

    @Override
    public String getModVersion() {
        return annotation.version();
    }

    @Override
    public String getDependencies() {
        return annotation.dependencies();
    }
}
