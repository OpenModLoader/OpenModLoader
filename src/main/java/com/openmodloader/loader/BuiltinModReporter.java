package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModCandidate;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.VoidModConfigurator;

public class BuiltinModReporter implements IModReporter {
    @Override
    public void apply(ModReportCollector collector, ModConstructor constructor) {
        ModMetadata vanillaMetadata = new ModMetadata("minecraft", Version.valueOf("1.14.0+18w43b"));
        collector.report(new ModCandidate(vanillaMetadata, new VoidModConfigurator()).global());

        ModMetadata omlMetadata = new ModMetadata("openmodloader", OpenModLoader.VERSION);
        collector.report(new ModCandidate(omlMetadata, new BuiltinModConfigurator()).global());
    }
}
