package com.openmodloader.loader.json;

import com.github.zafarkhaja.semver.Version;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class VersionTypeAdapter extends TypeAdapter<Version> {
    @Override
    public void write(JsonWriter out, Version value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Version read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return Version.valueOf(in.nextString());
    }
}
