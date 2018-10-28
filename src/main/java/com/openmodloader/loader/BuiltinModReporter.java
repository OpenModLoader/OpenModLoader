package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.VoidModConfigurator;

public class BuiltinModReporter implements IModReporter {
    @Override
    public void apply(ModReportCollector collector) {
        collector.report(new ModMetadata("minecraft", Version.valueOf("1.14.0+18w43b")), new VoidModConfigurator());
        collector.report(new ModMetadata("openmodloader", OpenModLoader.VERSION), new OmlBuiltinMod());
    }
}
