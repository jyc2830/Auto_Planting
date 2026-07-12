package com.autoplanting.util;

import org.bukkit.Material;

public final class CropUtils {
    private CropUtils() {
    }

    public static boolean isSupportedCropName(String name) {
        return "WHEAT".equals(name)
            || "CARROT".equals(name)
            || "CARROTS".equals(name)
            || "POTATO".equals(name)
            || "POTATOES".equals(name)
            || "BEETROOT".equals(name)
            || "BEETROOTS".equals(name)
            || "BEETROOT_BLOCK".equals(name)
            || "NETHER_WART".equals(name)
            || "NETHER_WARTS".equals(name);
    }

    public static Material safeMaterial(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            if (candidate == null) {
                continue;
            }
            Material material = Material.getMaterial(candidate);
            if (material != null) {
                return material;
            }
        }
        return null;
    }
}
