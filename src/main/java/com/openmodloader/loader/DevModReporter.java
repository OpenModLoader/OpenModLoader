package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DevModReporter implements IModReporter {
    private static final Logger LOGGER = LogManager.getLogger(DevModReporter.class);
    private static final JsonParser PARSER = new JsonParser();

    @Override
    public void apply(ModReportCollector collector) {
        try (InputStream input = OpenModLoader.class.getResourceAsStream("/mods.json")) {
            // TODO: A more generic system for parsing mods.json files
            JsonArray modArray = PARSER.parse(new InputStreamReader(input)).getAsJsonArray();
            for (JsonElement element : modArray) {
                JsonObject modRoot = element.getAsJsonObject();
                try {
                    reportMod(collector, modRoot);
                } catch (ReflectiveOperationException e) {
                    LOGGER.error("Failed to report dev mod from {}", element, e);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to report dev mods from mods.json", e);
        }
    }

    private void reportMod(ModReportCollector collector, JsonObject modRoot) throws ReflectiveOperationException {
        ModMetadata metadata = parseMetadata(modRoot);

        String configurator = modRoot.get("configurator").getAsString();
        Class<?> configuratorClass = Class.forName(configurator);

        if (!IModConfigurator.class.isAssignableFrom(configuratorClass)) {
            LOGGER.error("'{}' defined for mod '{}' was not of type IModConfigurator", configurator, metadata.getId());
            return;
        }

        collector.report(metadata, (IModConfigurator) configuratorClass.newInstance());
    }

    private ModMetadata parseMetadata(JsonObject modRoot) {
        String id = modRoot.get("id").getAsString();
        Version version = Version.valueOf(modRoot.get("version").getAsString());
        return new ModMetadata(id, version);
    }
}
