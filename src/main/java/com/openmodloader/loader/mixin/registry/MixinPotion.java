package com.openmodloader.loader.mixin.registry;

import com.openmodloader.api.registry.IRegistryEntry;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Potion.class)
public class MixinPotion implements IRegistryEntry<Potion> {
    private Identifier registryName;

    @Override
    public Identifier getRegistryName() {
        return registryName;
    }

    @Override
    public void setRegistryName(Identifier identifier) {
        if (this.registryName != null) {
            //Already named
        }
        this.registryName = identifier;
    }
}