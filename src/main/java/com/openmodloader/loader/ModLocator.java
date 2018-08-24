package com.openmodloader.loader;

import com.openmodloader.loader.locator.LoaderContext;

import java.net.MalformedURLException;
import java.util.Collection;

public interface ModLocator<B extends Mod.Blueprint> {

    String getName();

    /**
     * @param containers Collection of containers that have requested this locator
     * @return Collection of blueprints created from the containers
     */
    Collection<B> transform(Collection<ModContainer> containers);

    void identifySource(B blueprint, LoaderContext context) throws MalformedURLException;

    void initialize(B blueprint, LoaderContext context) throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}
