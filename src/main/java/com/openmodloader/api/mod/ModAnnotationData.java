package com.openmodloader.api.mod;

public class ModAnnotationData implements IModData {
    private Mod annotation;

    public ModAnnotationData(Class<?> mClass) {
        annotation= mClass.getAnnotation(Mod.class);
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
