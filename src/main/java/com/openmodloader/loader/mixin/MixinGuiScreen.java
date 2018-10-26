package com.openmodloader.loader.mixin;

import com.openmodloader.core.event.GuiRenderEvent;
import com.openmodloader.loader.OpenModLoader;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Rewrite(behavior = Rewrite.Behavior.END)
    public void draw(int mouseX, int mouseY, float aFloat3) {
        GuiScreen screen = (GuiScreen) (Object) this;
        OpenModLoader.get().getEventDispatcher().dispatch(new GuiRenderEvent<>(screen));
    }
}
