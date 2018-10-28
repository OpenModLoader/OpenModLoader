package com.openmodloader.loader;

import com.openmodloader.api.event.EventContext;
import com.openmodloader.api.mod.config.IModConfig;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.core.event.GuiEvent;
import net.minecraft.client.gui.menu.GuiMainMenu;
import net.minecraft.client.render.text.TextRenderer;

public class OmlBuiltinMod implements IModConfigurator {
    @Override
    public void configure(IModConfig config) {
        config.addEventConfig(SimpleEventConfig.builder()
                .listen(GuiEvent.Draw.target(GuiMainMenu.class), this::renderMainMenu)
                .build()
        );
    }

    private void renderMainMenu(GuiEvent.Draw<GuiMainMenu> event, EventContext context) {
        GuiMainMenu gui = event.getGui();
        TextRenderer textRenderer = event.getTextRenderer();

        int mods = OpenModLoader.get().getModList().size();
        String version = OpenModLoader.VERSION.toString();

        textRenderer.renderText(String.format("OML Version %s", version), 2, gui.height - 30, -1);
        textRenderer.renderText(String.format("%d %s Loaded", mods, mods == 1 ? "Mod" : "Mods"), 2, gui.height - 20, -1);
    }
}
