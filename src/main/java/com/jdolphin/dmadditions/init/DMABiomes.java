package com.jdolphin.dmadditions.init;

import com.jdolphin.dmadditions.DmAdditions;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DMABiomes {
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, DmAdditions.MODID);
	public DMABiomes() {}
	public static final RegistryObject<Biome> MOON_BIOME;
	public static RegistryKey<Biome> MOON;


	private static RegistryKey<Biome> makeKey(String name) {
		return RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(DmAdditions.MODID, name));
	}
	static {
		MOON_BIOME = BIOMES.register("moon", BiomeMaker::theVoidBiome);
	}
}
