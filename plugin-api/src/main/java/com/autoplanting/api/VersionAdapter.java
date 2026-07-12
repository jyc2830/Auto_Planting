package com.autoplanting.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface VersionAdapter {
    Material resolveCropMaterial(String logicalMaterial);

    boolean isSupportedCrop(Block block);

    boolean isMatureCrop(Block block);

    Material resolvePlantingMaterial(Material cropMaterial);

    void replant(Block block, Material cropMaterial);

    void sendActionBar(Player player, String message);

    void spawnParticle(Location location, String particleName, int count);
}
