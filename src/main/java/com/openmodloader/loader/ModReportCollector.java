package com.openmodloader.loader;

import com.openmodloader.api.mod.ModCandidate;

import java.util.ArrayList;
import java.util.Collection;

public class ModReportCollector {
    private final Collection<ModCandidate> candidates = new ArrayList<>();

    public void report(ModCandidate candidate) {
        this.candidates.add(candidate);
    }

    public Collection<ModCandidate> getCandidates() {
        return candidates;
    }
}
