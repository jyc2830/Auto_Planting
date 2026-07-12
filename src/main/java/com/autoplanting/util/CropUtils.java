package com.autoplanting.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.lang.reflect.Method;

public final class CropUtils {
    private CropUtils() {
    }

    public static boolean isSupportedCrop(Material material) {
        return material != null && getPlantingMaterial(material) != null;
    }

    public static Material getPlantingMaterial(Material crop) {
        if (crop == null) {
            return null;
        }
        String name = crop.name();
        if ("WHEAT".equals(name) || "CROPS".equals(name)) {
            return material("WHEAT_SEEDS", "SEEDS");
        }
        if ("CARROT".equals(name) || "CARROTS".equals(name)) {
            return material("CARROT");
        }
        if ("POTATO".equals(name) || "POTATOES".equals(name)) {
            return material("POTATO");
        }
        if ("BEETROOTS".equals(name) || "BEETROOT".equals(name) || "BEETROOT_BLOCK".equals(name)) {
            return material("BEETROOT_SEEDS");
        }
        if ("NETHER_WART".equals(name) || "NETHER_WARTS".equals(name)) {
            return material("NETHER_WART");
        }
        return null;
    }

    public static Material material(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            Material material = Material.getMaterial(candidate);
            if (material != null) {
                return material;
            }
        }
        return null;
    }

    public static boolean isMature(Block block) {
        if (block == null) {
            return false;
        }

        try {
            Method getBlockData = block.getClass().getMethod("getBlockData");
            Object data = getBlockData.invoke(block);
            Class<?> ageableClass = Class.forName("org.bukkit.block.data.Ageable");
            if (ageableClass.isInstance(data)) {
                Method getAge = ageableClass.getMethod("getAge");
                Method getMaximumAge = ageableClass.getMethod("getMaximumAge");
                return ((Integer) getAge.invoke(data)).intValue() >= ((Integer) getMaximumAge.invoke(data)).intValue();
            }
        } catch (Throwable ignored) {
        }

        byte data = block.getData();
        String name = block.getType().name();
        if ("CROPS".equals(name)) {
            return data >= 7;
        }
        if ("CARROT".equals(name) || "POTATO".equals(name)) {
            return data >= 7;
        }
        if ("BEETROOT_BLOCK".equals(name) || "BEETROOTS".equals(name)) {
            return data >= 3;
        }
        if ("NETHER_WART".equals(name)) {
            return data >= 3;
        }
        return true;
    }
}
