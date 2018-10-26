package com.openmodloader.api.loader;

import com.openmodloader.loader.ModReportCollector;

public interface IModReporter {
    void apply(ModReportCollector collector);
}
