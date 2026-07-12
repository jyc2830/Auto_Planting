package com.autoplanting.platform.version;

import com.autoplanting.api.VersionAdapter;
import com.autoplanting.util.CropUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public final class ReflectionVersionAdapter implements VersionAdapter {
    @Override
    public Material resolveCropMaterial(String logicalMaterial) {
        if ("wheat".equals(logicalMaterial)) {
            return CropUtils.safeMaterial("WHEAT", "CROPS");
        }
        if ("carrot".equals(logicalMaterial)) {
            return CropUtils.safeMaterial("CARROT", "CARROTS");
        }
        if ("potato".equals(logicalMaterial)) {
            return CropUtils.safeMaterial("POTATO", "POTATOES");
        }
        if ("beetroot".equals(logicalMaterial)) {
            return CropUtils.safeMaterial("BEETROOTS", "BEETROOT_BLOCK");
        }
        if ("nether_wart".equals(logicalMaterial)) {
            return CropUtils.safeMaterial("NETHER_WART", "NETHER_WARTS");
        }
        return null;
    }

    @Override
    public boolean isSupportedCrop(Block block) {
        return block != null && CropUtils.isSupportedCropName(block.getType().name());
    }

    @Override
    public boolean isMatureCrop(Block block) {
        try {
            Method getBlockData = block.getClass().getMethod("getBlockData");
            Object data = getBlockData.invoke(block);
            Class<?> ageable = Class.forName("org.bukkit.block.data.Ageable");
            if (ageable.isInstance(data)) {
                Method getAge = ageable.getMethod("getAge");
                Method getMaximumAge = ageable.getMethod("getMaximumAge");
                return ((Integer) getAge.invoke(data)).intValue() >= ((Integer) getMaximumAge.invoke(data)).intValue();
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Override
    public Material resolvePlantingMaterial(Material cropMaterial) {
        String name = cropMaterial == null ? null : cropMaterial.name();
        if ("WHEAT".equals(name) || "CROPS".equals(name)) {
            return CropUtils.safeMaterial("WHEAT_SEEDS", "SEEDS");
        }
        if ("CARROT".equals(name) || "CARROTS".equals(name)) {
            return CropUtils.safeMaterial("CARROT");
        }
        if ("POTATO".equals(name) || "POTATOES".equals(name)) {
            return CropUtils.safeMaterial("POTATO");
        }
        if ("BEETROOTS".equals(name) || "BEETROOT_BLOCK".equals(name)) {
            return CropUtils.safeMaterial("BEETROOT_SEEDS");
        }
        if ("NETHER_WART".equals(name) || "NETHER_WARTS".equals(name)) {
            return CropUtils.safeMaterial("NETHER_WART");
        }
        return null;
    }

    @Override
    public void replant(Block block, Material cropMaterial) {
        try {
            block.setType(cropMaterial);
            Method getBlockData = block.getClass().getMethod("getBlockData");
            Object data = getBlockData.invoke(block);
            Class<?> ageable = Class.forName("org.bukkit.block.data.Ageable");
            if (ageable.isInstance(data)) {
                Method setAge = ageable.getMethod("setAge", int.class);
                setAge.invoke(data, Integer.valueOf(0));
                Class<?> blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
                Method setBlockData = block.getClass().getMethod("setBlockData", blockDataClass, boolean.class);
                setBlockData.invoke(block, data, Boolean.FALSE);
            }
        } catch (Throwable ignored) {
            block.setType(cropMaterial);
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {
        try {
            player.sendMessage(message);
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void spawnParticle(Location location, String particleName, int count) {
        try {
            Bukkit.getWorlds().get(0).spawnParticle(org.bukkit.Particle.valueOf(particleName), location, count);
        } catch (Throwable ignored) {
        }
    }
}
