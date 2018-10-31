package com.openmodloader.loader.mixin;

import com.mojang.brigadier.CommandDispatcher;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.Rewrite;
import net.minecraft.command.CommandManager;
import net.minecraft.command.CommandSender;


@Mixin(CommandManager.class)
public abstract class MixinCommandManager {

	private CommandDispatcher<CommandSender> commandDispatcher;

	@Rewrite(target = "<init>", behavior = Rewrite.Behavior.END)
	public void constructor(boolean dediServer) {
		// TODO
//        OpenModLoader.LOAD_BUS.post(new RegisterCommandsEvent(commandDispatcher));
    }

}
