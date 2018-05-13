package com.openmodloader.loader;

import javax.annotation.Nonnull;
import java.util.jar.JarFile;

public class ModContainer {
    private final JarFile jar;
    private final ModInfo info;

    public ModContainer(@Nonnull JarFile jar, @Nonnull ModInfo info) {
        this.jar = jar;
        this.info = info;
    }

    public JarFile getJar() {
        return jar;
    }

    public ModInfo getInfo() {
        return info;
    }
}
