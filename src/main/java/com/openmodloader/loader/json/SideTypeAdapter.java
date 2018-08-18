package com.openmodloader.loader.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.Side;

import java.io.IOException;

public class SideTypeAdapter extends TypeAdapter<Side> {
    @Override
    public void write(JsonWriter out, Side value) throws IOException {
        out.value(value.name());
    }

    @Override
    public Side read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return Side.valueOf(in.nextString());
    }
}
