package com.openmodloader.loader.mixin;

        import net.minecraft.client.ClientBrandRetriever;
        import org.spongepowered.asm.mixin.Mixin;
        import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public abstract class MixinClientBrand {

    /**
     * @author Coded
     */
    @Overwrite
    public static String getClientModName() {
        return "OpenModLoader";
    }


}
