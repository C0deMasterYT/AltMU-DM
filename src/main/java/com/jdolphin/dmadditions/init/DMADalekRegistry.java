package com.jdolphin.dmadditions.init;

import com.jdolphin.dmadditions.advent.AdventUnlock;
import com.jdolphin.dmadditions.entity.dalek.types.*;
import com.swdteam.common.entity.dalek.DalekType;
import com.swdteam.common.entity.dalek.IDalek;
import com.swdteam.common.init.DMDalekRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swdteam.common.init.DMDalekRegistry.DALEK_TYPES;

public class DMADalekRegistry {
	private static List<String> dmaDalekList = new ArrayList();
	private static Map<String, IDalek> dmaDaleks = new HashMap();

	public static IDalek DALEK_SANTA;
	public static IDalek IRONSIDE;
	public static IDalek CANDYCANE;
	public static IDalek PFD;
	public static IDalek STORM;

	public static void init(List<String> dalekList, Map<String, IDalek> daleks) {
		if (AdventUnlock.unlockAt(24)) {
			DALEK_SANTA = addDalek(DMADalekType.SANTA, new DalekSantaBase("Dalek Santa"), "dalek_santa");
		}
		if (AdventUnlock.unlockAt(2)) {
			CANDYCANE = addDalek(DMADalekType.CANDYCANE, new CandycaneDalek("Candy Cane Dalek"), "lime_candycane_dalek");
			CANDYCANE.addChild("blue_candycane_dalek");
			CANDYCANE.addChild("red_candycane_dalek");
			CANDYCANE.addChild("orange_candycane_dalek");
		}
		if (AdventUnlock.unlockAt(12)) {
			PFD = addDalek(DMADalekType.PFD, new PFDDalekBase("Pink Fluffy Dalek"), "pink_fluffy_dalek");
			STORM = addDalek(DMADalekType.STORM, new StormDalekBase("Dalek Storm"), "dalek_storm");

		}
		if (AdventUnlock.unlockAt(22)) {
			IRONSIDE = addDalek(DMADalekType.IRONSIDE, new IronsideDalekBase("Ironside Dalek"), "ironside_dalek");
		}

		dalekList.addAll(dmaDalekList);
		daleks.putAll(dmaDaleks);
	}

	private static IDalek addDalek(DalekType type, IDalek dalek, String registryKey) {
		try {
			dalek.setRegistryName(type, registryKey);
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		register(registryKey, dalek);
		return dalek;
	}

	private static void register(String registryKey, IDalek dalek) {
		dmaDaleks.put(registryKey, dalek);
		dmaDalekList.add(registryKey);
		if (!DALEK_TYPES.containsKey(dalek.getType())) {
			DALEK_TYPES.put(dalek.getType(), new ArrayList<>());
		}

		DALEK_TYPES.get(dalek.getType()).add(dalek.getID());
	}
}
