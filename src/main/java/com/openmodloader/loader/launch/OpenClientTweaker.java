package com.openmodloader.loader.launch;

import net.fabricmc.api.Side;

import java.io.File;
import java.util.List;

public class OpenClientTweaker extends OpenTweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        super.acceptOptions(args, gameDir, assetsDir, profile);

        if (!this.args.containsKey("--assetsDir") && assetsDir != null) {
            this.args.put("--assetsDir", assetsDir.getAbsolutePath());
        }

        if (!this.args.containsKey("--accessToken")) {
            this.args.put("--accessToken", "OpenModLoader");
        }
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }
}
