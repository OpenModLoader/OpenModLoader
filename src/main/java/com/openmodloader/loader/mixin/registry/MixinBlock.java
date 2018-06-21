package com.openmodloader.loader.mixin.registry;

import com.openmodloader.api.registry.IRegistryEntry;
import com.openmodloader.core.registry.RegistryEvent;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock implements IRegistryEntry<Block> {
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

    @Inject(method = "registerBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultMappedRegistry;checkDefault()V", shift = At.Shift.BEFORE))
    private static void registerBlocks(CallbackInfo info) {
        OpenModLoader.LOAD_BUS.post(new RegistryEvent<>(Block.REGISTRY, Block.class));
    }
}