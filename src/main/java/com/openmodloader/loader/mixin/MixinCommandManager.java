package com.openmodloader.loader.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.openmodloader.core.registry.RegisterCommandsEvent;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.command.CommandManager;
import net.minecraft.command.CommandSender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager {
    @Shadow
    @Final
    private CommandDispatcher<CommandSender> commandDispatcher;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void constructor(boolean dediServer, CallbackInfo info) {
        OpenModLoader.LOAD_BUS.post(new RegisterCommandsEvent(commandDispatcher));
    }

}
