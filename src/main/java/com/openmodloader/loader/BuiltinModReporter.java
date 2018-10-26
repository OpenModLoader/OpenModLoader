package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.api.mod.config.VoidModConfigurator;
import com.openmodloader.core.event.GuiEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.menu.GuiMainMenu;

public class BuiltinModReporter implements IModReporter {
    @Override
    public void apply(ModReportCollector collector) {
        collector.report(new ModMetadata("minecraft", Version.valueOf("1.14.0+18w43b")), new VoidModConfigurator());
        collector.report(new ModMetadata("openmodloader", Version.valueOf("1.0.0")), config -> {
            // TODO: Should be extracted out into another class
            config.addEventConfig(SimpleEventConfig.builder()
                    .listen(GuiEvent.Open.target(GuiScreen.class), (event, context) -> {
                        GuiScreen gui = event.getGui();
                        System.out.println("opened screen " + gui);
                    })
                    .listen(GuiEvent.Draw.target(GuiMainMenu.class), (event, context) -> {
                        GuiMainMenu gui = event.getGui();
                        System.out.println("draw screen " + gui);
                    })
                    .build()
            );
        });
    }
}
