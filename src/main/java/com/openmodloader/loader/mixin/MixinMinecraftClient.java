package com.openmodloader.loader.mixin;

import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.client.ClientSideHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    public void init(CallbackInfo info) throws IOException {
        OpenModLoader.initialize(MinecraftClient.getInstance().runDirectory, new ClientSideHandler());
    }
}
