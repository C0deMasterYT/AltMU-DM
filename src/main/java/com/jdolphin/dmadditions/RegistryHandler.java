package com.jdolphin.dmadditions;

import com.jdolphin.dmadditions.init.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	public static DMABiomes dmaBiomes;
	public static DMAEntities dmaEntities;
	public static DMABlockEntities dmaTiles;
	public static DMAWorldCarvers dmaCarvers;
	public static DMAItems dmaItems;
	public static DMASoundEvents dmaSounds;
	public static DMABlocks dmaBlocks;
	public static DMALootConditionManager dmaLootConditionManager;


	public static void init() {
		DMAProjectiles.init();

		dmaCarvers = new DMAWorldCarvers();
		dmaBiomes = new DMABiomes();
		dmaSounds = new DMASoundEvents();
		dmaItems = new DMAItems();
		dmaBlocks = new DMABlocks();
		dmaTiles = new DMABlockEntities();
		dmaEntities = new DMAEntities();
		dmaLootConditionManager = new DMALootConditionManager();

		DMARegistries.register();
	}

	public static class DMARegistries{
		public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
		public static final DeferredRegister<Item> ITEMS;

		public static void register(){
			IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

			ENTITY_TYPES.register(modEventBus);
			ITEMS.register(modEventBus);
		}

		static{
			ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, DmAdditions.MODID);
			ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DmAdditions.MODID);
		}
	}
}
