package com.openmodloader.core.data;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.openmodloader.api.DataHandler;
import com.openmodloader.api.DataObject;
import com.openmodloader.loader.OpenModLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JsonDataHandler implements DataHandler {
    @Override
    public DataObject read(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        JsonObject json = OpenModLoader.getGson().fromJson(reader, JsonObject.class);
        return new JSONDataObject(json);
    }

    public static final class JSONDataObject implements DataObject {
        private static final JSONDataObject EMPTY = new JSONDataObject(null);

        private final JsonObject object;

        JSONDataObject(JsonObject object) {
            this.object = object;
        }

        @Override
        public boolean isPresent() {
            return object != null;
        }

        @Override
        public void ifPresent(Consumer<DataObject> consumer) {
            if (isPresent())
                consumer.accept(this);
        }

        @Override
        public DataObject getChild(String name) {
            if (!isPresent())
                return EMPTY;
            if (!object.has(name))
                return EMPTY;
            JsonElement element = object.get(name);
            return element.isJsonObject() ? new JSONDataObject(element.getAsJsonObject()) : EMPTY;
        }

        @Override
        public DataArray getArray(String name) {
            if (!isPresent())
                return JSONDataArray.EMPTY;
            if (!object.has(name))
                return JSONDataArray.EMPTY;
            JsonElement element = object.get(name);
            return element.isJsonArray() ? new JSONDataArray(element.getAsJsonArray()) : JSONDataArray.EMPTY;
        }

        @Override
        public DataElement get(String name) {
            if (!isPresent())
                return JSONDataElement.EMPTY;
            if (!object.has(name))
                return JSONDataElement.EMPTY;

            JsonElement element = object.get(name);
            if (!element.isJsonPrimitive()) return JSONDataElement.EMPTY;
            return new JSONDataElement(element);
        }

        @Override
        public <T> T get(String name, Class<T> type) {
            return get(name).as(type);
        }

        @Override
        public <T> T orElse(String name, Class<T> type, T other) {
            return get(name).orElse(type, other);
        }

        @Override
        public <T> T orElseGet(String name, Class<T> type, Supplier<T> supplier) {
            return get(name).orElseGet(type, supplier);
        }
    }

    public static class JSONDataArray implements DataObject.DataArray {
        private static final JSONDataArray EMPTY = new JSONDataArray(null);

        private final JsonArray array;

        JSONDataArray(JsonArray array) {
            this.array = array;
        }

        @Override
        public boolean isPresent() {
            return array != null;
        }

        @Override
        public void ifPresent(Consumer<DataObject.DataArray> consumer) {
            if (isPresent())
                consumer.accept(this);
        }

        @Override
        public DataObject.DataElement get(int i) throws IndexOutOfBoundsException {
            if (!isPresent())
                return JSONDataElement.EMPTY;
            return new JSONDataElement(array.get(i));
        }

        @Override
        public Collection<DataObject.DataElement> getAll() {
            return Streams.stream(array.iterator()).map(JSONDataElement::new).collect(Collectors.toSet());
        }
    }

    public static class JSONDataElement implements DataObject.DataElement {
        private static final JSONDataElement EMPTY = new JSONDataElement(null);
        private final JsonElement element;

        JSONDataElement(JsonElement element) {
            this.element = element;
        }

        @Override
        public boolean isPresent() {
            return element != null;
        }

        @Override
        public void ifPresent(Consumer<DataObject.DataElement> consumer) {
            if (isPresent())
                consumer.accept(this);
        }

        @Override
        public <T> Optional<T> map(Function<DataObject.DataElement, T> mapper) {
            if (!isPresent())
                return Optional.empty();
            return Optional.ofNullable(mapper.apply(this));
        }

        @Override
        public <T> T as(Class<T> type) throws NullPointerException {
            T obj = orElse(type, null);
            if (obj == null) throw new NullPointerException();
            return obj;
        }

        @Override
        public <T> T orElse(Class<T> type, T other) {
            if (!isPresent())
                return other;
            if (type == int.class || type == Integer.class) return type.cast(element.getAsInt());
            if (type == short.class || type == Short.class) return type.cast(element.getAsShort());
            if (type == long.class || type == Long.class) return type.cast(element.getAsLong());
            if (type == byte.class || type == Byte.class) return type.cast(element.getAsByte());
            if (type == char.class || type == Character.class) return type.cast(element.getAsCharacter());
            if (type == float.class || type == Float.class) return type.cast(element.getAsFloat());
            if (type == double.class || type == Double.class) return type.cast(element.getAsDouble());
            if (type == boolean.class || type == Boolean.class) return type.cast(element.getAsBoolean());
            if (type == String.class) return type.cast(element.getAsString());
            return other;
        }

        @Override
        public <T> T orElseGet(Class<T> type, Supplier<T> supplier) {
            T obj = orElse(type, null);
            if (obj == null) return supplier.get();
            return obj;
        }
    }
}