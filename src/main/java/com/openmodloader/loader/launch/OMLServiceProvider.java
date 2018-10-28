package com.openmodloader.loader.launch;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import me.modmuss50.fusion.MixinManager;
import me.modmuss50.fusion.transformer.MixinTransformer;
import net.fabricmc.api.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.*;

public class OMLServiceProvider implements ITransformationService {

	protected static Logger LOGGER = LogManager.getFormatterLogger("OpenModLoader");

	@Nonnull
	@Override
	public String name() {
		return "oml";
	}

	@Override
	public void initialize(IEnvironment environment) {
		LOGGER.info("Hello Mod Launcher!");
		Map<String, String> data = new HashMap<>();
		data.put("side", (System.getProperty("omlServer", "false").equals("true") ? Side.SERVER : Side.CLIENT).name()); //TODO find a good way to get the side
		MixinManager.findMixins(data);
	}

	@Override
	public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

	}

	@Nonnull
	@Override
	public List<ITransformer> transformers() {
		List<ITransformer> transformers = new ArrayList<>();
		transformers.add(new MixinTransformer());
		return transformers;
	}
}
