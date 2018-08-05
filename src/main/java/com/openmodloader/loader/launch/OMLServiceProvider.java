package com.openmodloader.loader.launch;

import com.openmodloader.loader.transformer.AccessTransformer;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import me.modmuss50.fusion.transformer.MixinTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

		try {
			MixinLoader.initMixins(new File("mods"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to find mods", e);
		}

	}

	@Override
	public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

	}

	@Nonnull
	@Override
	public List<ITransformer> transformers() {
		List<ITransformer> transformers = new ArrayList<>();
		transformers.add(new MixinTransformer());
		transformers.add(new AccessTransformer());
		return transformers;
	}
}
