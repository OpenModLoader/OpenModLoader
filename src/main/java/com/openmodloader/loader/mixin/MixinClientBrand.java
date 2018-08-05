package com.openmodloader.loader.mixin;

import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import net.minecraft.client.ClientBrandRetriever;

@Mixin(ClientBrandRetriever.class)
public abstract class MixinClientBrand {

    /**
     * @reason Change modification branding
     * @author Coded
     */
    @Inject
    public static String getClientModName() {
        return "OpenModLoader";
    }


}
