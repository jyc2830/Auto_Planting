package com.autoplanting.listener;

import com.autoplanting.util.CropUtils;
import com.autoplanting.util.ReplantTaskRunner;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class CropHarvestListener implements Listener {
    private final ReplantTaskRunner taskRunner;

    public CropHarvestListener(org.bukkit.plugin.Plugin plugin) {
        this.taskRunner = new ReplantTaskRunner(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        if (!CropUtils.isSupportedCrop(type)) {
            return;
        }

        if (!CropUtils.isMature(block)) {
            if (!event.getPlayer().isSneaking()) {
                event.setCancelled(true);
            }
            return;
        }

        Player player = event.getPlayer();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        Material plantingMaterial = CropUtils.getPlantingMaterial(type);
        if (plantingMaterial == null || offhand.getType() != plantingMaterial || offhand.getAmount() <= 0) {
            return;
        }

        taskRunner.replantLater(player, block.getLocation(), type, plantingMaterial);
    }
}
