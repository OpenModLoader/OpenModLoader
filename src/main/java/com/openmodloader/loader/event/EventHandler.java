package com.openmodloader.loader.event;

import com.openmodloader.api.event.Event;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.client.gui.menu.GuiMainMenu;

public class EventHandler {
    @Event.Subscribe
    public void drawMainMenu(GuiEvent.Draw<GuiMainMenu> event) {
        int mods = OpenModLoader.getActiveModIds().size();
        event.getFontRenderer().drawString(String.format("%d %s Loaded", mods, mods == 1 ? "Mod" : "Mods"), 2, event.getGui().height - 20, -1);
        event.getFontRenderer().drawString(String.format("Loader Version %s", OpenModLoader.getVersion()), 2, event.getGui().height - 30, -1);
    }
}