package com.openmodloader.loader;

import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfigurator;

import java.util.ArrayList;
import java.util.Collection;

public class ModReportCollector {
    private final Collection<Report> reports = new ArrayList<>();

    public void report(ModMetadata metadata, IModConfigurator configurator) {
        this.reports.add(new Report(metadata, configurator));
    }

    public Collection<Report> getReports() {
        return reports;
    }

    public static class Report {
        private final ModMetadata metadata;
        private final IModConfigurator configurator;

        private Report(ModMetadata metadata, IModConfigurator configurator) {
            this.metadata = metadata;
            this.configurator = configurator;
        }

        public ModMetadata getMetadata() {
            return metadata;
        }

        public IModConfigurator getConfigurator() {
            return configurator;
        }
    }
}
