package com.openmodloader.loader.mixin.registry;

import com.openmodloader.api.registry.IRegistryEntry;
import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;


@Mixin(Block.class)
public class MixinBlock implements IRegistryEntry<Block> {
    private Identifier registryName;

	@Inject
	@Override
	public Identifier getRegistryName() {
		return registryName;
	}

	@Inject
	@Override
	public void setRegistryName(Identifier identifier) {
		if (this.registryName != null) {
			//Already named
		}
		this.registryName = identifier;
	}
}