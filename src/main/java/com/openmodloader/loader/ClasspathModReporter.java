package com.openmodloader.loader;

import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.loader.parse.ModDeclaration;
import com.openmodloader.loader.parse.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class ClasspathModReporter implements IModReporter {
    private static final Logger LOGGER = LogManager.getLogger(ClasspathModReporter.class);

    @Override
    public void apply(ModReportCollector collector, ModConstructor constructor) {
        try {
            Enumeration<URL> resources = ClassLoader.getSystemResources("mod.xml");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream input = url.openStream()) {
                    reportMod(collector, constructor, ModDeclaration.parse(input));
                } catch (ParseException e) {
                    LOGGER.error("Failed to parse mod.xml file", e);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to retrieve mod.xml files from classpath", e);
        }
    }

    private void reportMod(ModReportCollector collector, ModConstructor constructor, ModDeclaration declaration) {
        try {
            ModMetadata metadata = declaration.buildMetadata();

            IModConfigurator configurator = declaration.constructConfigurator(constructor);
            collector.report(metadata, configurator);

            for (ModDeclaration child : declaration.getChildren()) {
                reportMod(collector, constructor, child);
            }
        } catch (ModConstructionException e) {
            LOGGER.error("Failed to construct classpath mod '{}'", declaration.getId(), e);
        }
    }
}
