package com.openmodloader.loader.locator;

import com.openmodloader.loader.Mod;

import java.net.URL;

public interface LoaderContext {
    void addSource(URL url);

    void load(Mod mod);

    ClassLoader getClassLoader();
}
