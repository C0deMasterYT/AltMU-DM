package com.jdolphin.dmadditions.config;

import com.jdolphin.dmadditions.client.title.vortex.Vortex;
import com.jdolphin.dmadditions.client.title.vortex.VortexType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Random;


public final class DMAClientConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static ForgeConfigSpec.ConfigValue<Boolean> dma_classic;

	static {
		BUILDER.push("Dalek Mod: Additions Client Config");
		dma_classic = BUILDER.comment("Use classic Dalek Mod (1.12) background images on title screen").define("classic", false);

		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
