package com.openmodloader.api.mod.config;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.registry.IdRegistry;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Function;

public class SimpleRegistrationConfig implements IRegistrationConfig {
    private final ImmutableMultimap<IdRegistry<?>, RegistryEntrySupplier<?>> entries;

    private SimpleRegistrationConfig(ImmutableMultimap<IdRegistry<?>, RegistryEntrySupplier<?>> entries) {
        this.entries = entries;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerEntries(IdRegistry<T> registry) {
        Collection<RegistryEntrySupplier<?>> registryEntries = this.entries.get(registry);
        for (RegistryEntrySupplier<?> entry : registryEntries) {
            ((RegistryEntrySupplier<T>) entry).registerTo(registry);
        }
    }

    public static class Builder {
        private final ImmutableMultimap.Builder<IdRegistry<?>, RegistryEntrySupplier<?>> entries = ImmutableMultimap.builder();

        private Builder() {
        }

        public <T> Builder withEntry(IdRegistry<T> registry, Identifier identifier, Function<Identifier, T> supplier) {
            this.entries.put(registry, new RegistryEntrySupplier<>(identifier, supplier));
            return this;
        }

        public SimpleRegistrationConfig build() {
            return new SimpleRegistrationConfig(this.entries.build());
        }
    }

    private static class RegistryEntrySupplier<T> {
        private final Identifier identifier;
        private final Function<Identifier,T> supplier;

        private RegistryEntrySupplier(Identifier identifier, Function<Identifier, T> supplier) {
            this.identifier = identifier;
            this.supplier = supplier;
        }

        private void registerTo(IdRegistry<T> registry) {
            registry.register(this.identifier, this.supplier.apply(this.identifier));
        }
    }
}
