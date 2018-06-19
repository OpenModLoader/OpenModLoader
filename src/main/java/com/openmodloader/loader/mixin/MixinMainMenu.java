package com.openmodloader.loader.mixin;

import com.openmodloader.loader.OpenModLoader;
import com.openmodloader.loader.event.GuiEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.menu.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinMainMenu {

	@Inject(method = "draw(IIF)V", at = @At("RETURN"))
    public void draw(int mouseX, int mouseY, float delta, CallbackInfo i) {
		OpenModLoader.EVENT_BUS.post(new GuiEvent.Draw<>((GuiScreen) (Object) this));
    }
}