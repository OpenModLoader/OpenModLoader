package com.openmodloader.loader.locator;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.loader.Mod;
import com.openmodloader.loader.ModContainer;
import com.openmodloader.loader.ModLocator;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultModLocator implements ModLocator<DefaultModLocator.Blueprint> {
    @Override
    public String getName() {
        return "default";
    }

    @Override
    public Collection<Blueprint> transform(Collection<ModContainer> containers) {
        return containers.stream().map(Blueprint::new).collect(Collectors.toSet());
    }

    @Override
    public void identifySource(Blueprint blueprint, LoaderContext context) throws MalformedURLException {
        context.addSource(blueprint.getContainer().getPath().toUri().toURL());
    }

    @Override
    public void initialize(Blueprint blueprint, LoaderContext context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> mainClass = Class.forName(blueprint.mainClass, true, context.getClassLoader());
        if (!Mod.class.isAssignableFrom(mainClass))
            throw new RuntimeException();
        Class<? extends Mod> modClass = mainClass.asSubclass(Mod.class);
        Mod mod = modClass.newInstance();
        context.load(mod);
    }

    public static class Blueprint implements Mod.Blueprint {
        private final String mainClass;
        private final ModContainer container;
        private final String modId, modName;
        private final Version version;

        public Blueprint(ModContainer container) {
            this.container = container;
            mainClass = container.getMetadata().get("mainClass", String.class);
            modId = container.getMetadata().get("modid", String.class);
            modName = container.getMetadata().get("name", String.class);
            version = Version.valueOf(container.getMetadata().get("version", String.class));
        }

        @Override
        public ModContainer getContainer() {
            return container;
        }

        @Override
        public String getModId() {
            return modId;
        }

        @Override
        public String getName() {
            return modName;
        }

        @Override
        public Version getVersion() {
            return version;
        }
    }
}
