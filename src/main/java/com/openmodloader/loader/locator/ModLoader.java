package com.openmodloader.loader.locator;

import com.openmodloader.api.DataObject;
import com.openmodloader.loader.*;
import cpw.mods.modlauncher.ServiceLoaderStreamUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ModLoader {

    public static List<ModLocator> getModLocators() {
        return ServiceLoaderStreamUtils.toList(ServiceLoader.load(ModLocator.class));
    }

    public static Optional<Mod> getModInstance(String modid) {
        return Optional.empty();
    }

    public static void loadMods() {
        Map<String, ModLocator<?>> locators = getModLocators().stream().collect(Collectors.toMap(ModLocator::getName, s -> s));
        Map<String, List<ModContainer>> containers = new HashMap<>();
        try {
            collectContainersFromFolder(new File(OpenModLoader.getGameDirectory(), "mods")).forEach(container -> {
                String locator = container.getMetadata().orElse("locator", String.class, "default");
                if (!containers.containsKey(locator))
                    containers.put(locator, new ArrayList<>());
                containers.get(locator).add(container);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<Mod.Blueprint>> blueprintMap = new HashMap<>();
        containers.keySet().forEach(s -> {
            if (!blueprintMap.containsKey(s))
                blueprintMap.put(s, new ArrayList<>());
            blueprintMap.get(s).addAll(locators.get(s).transform(containers.get(s)));
        });
    }

    public static Collection<ModContainer> collectContainersFromFolder(File folder) throws IOException {
        Set<ModContainer> containers = new HashSet<>();
        for (File file : FileUtils.listFiles(folder, new String[]{"jar"}, true)) {
            JarFile jar = new JarFile(file);
            JarEntry modJson = jar.getJarEntry("mod.json");
            if (modJson == null)
                continue;
            DataObject modMeta = OpenModLoader.getJsonDataHandler().read(jar.getInputStream(modJson));
            containers.add(new JarModContainer(new File(folder, jar.getName()).toPath(), modMeta));
        }
        return containers;
    }


    public static class LoaderContextImpl implements LoaderContext {

        private Mod.Blueprint blueprint;
        private List<URL> sources = new ArrayList<>();
        private Mod mod;

        public LoaderContextImpl(Mod.Blueprint blueprint) {
            this.blueprint = blueprint;
        }

        @Override
        public void addSource(URL url) {
            sources.add(url);
        }

        @Override
        public void load(Mod mod) {
            this.mod = mod;
        }

        @Override
        public ClassLoader getClassLoader() {
            //TODO: Make this give each mod its own loader
            return getClass().getClassLoader();
        }
    }
}
