package com.openmodloader.loader.mixin;

import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.client.ClientSideHandler;
import com.openmodloader.loader.event.GuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo info) throws IOException {
        OpenModLoader.initialize(Minecraft.getInstance().runDirectory, new ClientSideHandler());
    }

    @Inject(method = "openGui", at = @At("RETURN"), remap = false)
    public void openGui(GuiScreen screen, CallbackInfo info) {
        if (screen != null)
            OpenModLoader.EVENT_BUS.post(new GuiEvent.Open<>(screen));
    }
}