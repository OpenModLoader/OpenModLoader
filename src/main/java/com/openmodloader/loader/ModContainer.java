package com.openmodloader.loader;

public class ModContainer {
    private final ModInfo info;
    private final Object modInstance;

    public ModContainer(ModInfo info, Object modInstance) {
        this.info = info;
        this.modInstance = modInstance;
    }

    public ModInfo getInfo() {
        return info;
    }

    public Object getModInstance() {
        return modInstance;
    }
}
