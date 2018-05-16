package com.openmodloader.core.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.loader.event.LoadEvent;
import net.minecraft.command.CommandSender;

public class RegisterCommandsEvent extends LoadEvent implements Event.PhaseLimit {
    private CommandDispatcher<CommandSender> dispatcher;

    public RegisterCommandsEvent(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public EventPhase[] getPossiblePhases() {
        return new EventPhase[]{EventPhase.DEFAULT};
    }

    public CommandDispatcher<CommandSender> getCommandDispatcher() {
        return dispatcher;
    }
}
