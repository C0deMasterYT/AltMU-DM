package com.jdolphin.dmadditions.init;

import com.jdolphin.dmadditions.item.TardisRemoteKeyItem;
import com.swdteam.common.RegistryHandler;
import com.swdteam.common.init.DMItemTiers;
import com.swdteam.common.init.DMSoundEvents;
import com.swdteam.common.init.DMTabs;
import com.swdteam.common.item.FoodItem;
import com.swdteam.common.item.LasergunItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;


public class DMAItems {

	public static RegistryObject<Item> DINO_NUGGETS;
	public static RegistryObject<Item> DINO_NUGGETS_CUSTARD;
	public static RegistryObject<Item> PISTOL;
	public static RegistryObject<Item> TARDIS_GOLD_KEY;

	static {
		PISTOL = RegistryHandler.ITEMS.register("pistol",
			() -> new LasergunItem(DMItemTiers.DALEK_GUNSTICK, 0.15F, DMAProjectiles.METALLIC_GOLD_LASER, DMSoundEvents.ENTITY_DALEK_GUNSTICK_CHARGE,
				DMASoundEvents.PISTOL_SHOOT, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT)));

		TARDIS_GOLD_KEY = RegistryHandler.ITEMS.register("tardis_gold_key",
			() -> new TardisRemoteKeyItem((new Item.Properties()).durability(24).tab(DMTabs.DM_TARDIS), ""));

		DINO_NUGGETS = RegistryHandler.ITEMS.register("dino_nuggets",
			() -> new FoodItem((new Item.Properties()).food(DMAFoods.DINO_NUGGETS).tab(ItemGroup.TAB_FOOD)));

		DINO_NUGGETS_CUSTARD = RegistryHandler.ITEMS.register("dino_nuggets_custard",
			() -> new FoodItem((new Item.Properties()).food(DMAFoods.DINO_NUGGETS_CUSTARD).tab(ItemGroup.TAB_FOOD)));
	}
}
