package com.openmodloader.loader.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.openmodloader.api.event.Event;
import com.openmodloader.core.registry.RegisterCommandsEvent;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.client.gui.menu.GuiMainMenu;
import net.minecraft.command.CommandManager;
import net.minecraft.command.CommandSender;
import net.minecraft.text.TextComponentString;

public class EventHandler {
    @Event.Subscribe
    public void drawMainMenu(GuiEvent.Draw<GuiMainMenu> event) {
        int mods = OpenModLoader.getActiveModIds().size();
        event.getFontRenderer().drawString(String.format("%d %s Loaded", mods, mods == 1 ? "Mod" : "Mods"), 2, event.getGui().height - 20, -1);
        event.getFontRenderer().drawString(String.format("Loader Version %s", OpenModLoader.getVersion()), 2, event.getGui().height - 30, -1);
    }

    @Event.Subscribe
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSender> dispatcher = event.getCommandDispatcher();
        LiteralArgumentBuilder<CommandSender> builder = CommandManager.newArgument("openmodloader");
        builder.then(CommandManager.newArgument("version").executes(context -> {
            context.getSource().a(new TextComponentString("OpenModLoader " + OpenModLoader.getVersion()), true);
            return 0;
        }));
        dispatcher.register(builder.executes(context -> 0));
    }
}