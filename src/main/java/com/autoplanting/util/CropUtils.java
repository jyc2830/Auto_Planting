package com.autoplanting.util;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public final class CropUtils {
    private static final Map<Material, Material> REPLANT_MATERIALS = new EnumMap<>(Material.class);

    static {
        REPLANT_MATERIALS.put(Material.WHEAT, Material.WHEAT_SEEDS);
        REPLANT_MATERIALS.put(Material.CARROTS, Material.CARROT);
        REPLANT_MATERIALS.put(Material.POTATOES, Material.POTATO);
        REPLANT_MATERIALS.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
        REPLANT_MATERIALS.put(Material.NETHER_WART, Material.NETHER_WART);
    }

    private CropUtils() {
    }

    public static boolean isSupportedCrop(Material material) {
        return REPLANT_MATERIALS.containsKey(material);
    }

    public static Material getPlantingMaterial(Material crop) {
        return REPLANT_MATERIALS.get(crop);
    }

    public static boolean canReplant(Material crop, Material heldItem) {
        return getPlantingMaterial(crop) == heldItem;
    }
}
