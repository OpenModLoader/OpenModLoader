package com.openmodloader.loader;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.loader.IModReporter;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.SimpleEventConfig;
import com.openmodloader.api.mod.config.VoidModConfigurator;
import com.openmodloader.core.event.GuiEvent;
import net.minecraft.client.gui.menu.GuiMainMenu;
import net.minecraft.client.render.text.TextRenderer;

public class BuiltinModReporter implements IModReporter {
    @Override
    public void apply(ModReportCollector collector) {
        collector.report(new ModMetadata("minecraft", Version.valueOf("1.14.0+18w43b")), new VoidModConfigurator());
        collector.report(new ModMetadata("openmodloader", Version.valueOf("1.0.0")), config -> {
            // TODO: Should be extracted out into another class
            config.addEventConfig(SimpleEventConfig.builder()
                    .listen(GuiEvent.Draw.target(GuiMainMenu.class), (event, context) -> {
                        GuiMainMenu gui = event.getGui();
                        TextRenderer textRenderer = event.getTextRenderer();

                        // TODO: Properly retrieve version
                        int mods = OpenModLoader.get().getModList().size();
                        textRenderer.renderText(String.format("%d %s Loaded", mods, mods == 1 ? "Mod" : "Mods"), 2, gui.height - 20, -1);
                        textRenderer.renderText(String.format("Loader Version %s", "1.0.0"), 2, gui.height - 30, -1);
                    })
                    .build()
            );
        });
    }
}
