package com.openmodloader.loader.exception;

import java.util.List;
import java.util.Map;

public class MissingModsException extends RuntimeException {
    public MissingModsException(Map<String, List<String>> missing, Map<String, List<String>> wrongVersion) {
        super(errorsToString(missing, wrongVersion));
    }

    public static String errorsToString(Map<String, List<String>> missing, Map<String, List<String>> wrongVersion) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        if (!missing.isEmpty()) {
            builder.append("    Missing Mods: \n");
            for (String mod : missing.keySet()) {
                builder.append("      ").append(mod).append(" Requires: \n");
                for (String missingMod : missing.get(mod)) {
                    builder.append("        ").append(missingMod).append("\n");
                }
            }
        }
        if (!wrongVersion.isEmpty()) {
            builder.append("    Wrong Version Mods: \n");
            for (String mod : wrongVersion.keySet()) {
                builder.append("      ").append(mod).append(" Requires: \n");
                for (String missingMod : wrongVersion.get(mod)) {
                    builder.append("        ").append(missingMod).append("\n");
                }
            }
        }
        return builder.toString();
    }
}