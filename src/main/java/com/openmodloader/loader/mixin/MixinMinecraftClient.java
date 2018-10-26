package com.openmodloader.loader.mixin;

import com.openmodloader.core.event.GuiEvent;
import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.client.ClientOMLContext;
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
	    OpenModLoader oml = OpenModLoader.initialize(new ClientOMLContext());
	    oml.loadMods();
    }

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void openGui(GuiScreen screen) {
        if (screen != null) {
            OpenModLoader.get().getEventDispatcher().dispatch(new GuiEvent.Open<>(screen));
        }
    }
}
