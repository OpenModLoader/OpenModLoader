package com.openmodloader.core.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import net.minecraft.command.CommandSender;

public class RegisterCommandsEvent implements Event.PhaseLimit {
    private CommandDispatcher<CommandSender> dispatcher;

    public RegisterCommandsEvent(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void register(LiteralArgumentBuilder<CommandSender> command) {
        dispatcher.register(command);
    }

    @Override
    public EventPhase[] getPossiblePhases() {
        return new EventPhase[]{EventPhase.DEFAULT};
    }

    public CommandDispatcher<CommandSender> getCommandDispatcher() {
        return dispatcher;
    }
}
