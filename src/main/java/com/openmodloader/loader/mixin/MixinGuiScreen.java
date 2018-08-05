package com.openmodloader.loader.mixin;

import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.event.GuiEvent;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

	@Rewrite(behavior = Rewrite.Behavior.END)
	public void draw(int aInteger1, int aInteger2, float aFloat3) {
		OpenModLoader.EVENT_BUS.post(new GuiEvent.Draw<>((GuiScreen) (Object) this));
	}
}
