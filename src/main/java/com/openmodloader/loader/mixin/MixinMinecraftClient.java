package com.openmodloader.loader.mixin;

import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.client.ClientSideHandler;
import com.openmodloader.loader.event.GuiEvent;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {

	@Rewrite(target = "<init>", behavior = Rewrite.Behavior.END)
	public void init(RunArgs aRunArgs) throws IOException {
        OpenModLoader.initialize(Minecraft.getInstance().runDirectory, new ClientSideHandler());
    }

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void openGui(GuiScreen screen) {
        if (screen != null)
            OpenModLoader.EVENT_BUS.post(new GuiEvent.Open<>(screen));
    }
}