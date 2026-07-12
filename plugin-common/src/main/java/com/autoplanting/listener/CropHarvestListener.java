package com.autoplanting.listener;

import com.autoplanting.api.PlatformScheduler;
import com.autoplanting.api.VersionAdapter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class CropHarvestListener implements Listener {
    private final Plugin plugin;
    private final PlatformScheduler scheduler;
    private final VersionAdapter versionAdapter;

    public CropHarvestListener(Plugin plugin, PlatformScheduler scheduler, VersionAdapter versionAdapter) {
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.versionAdapter = versionAdapter;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (!versionAdapter.isSupportedCrop(block)) {
            return;
        }

        if (!versionAdapter.isMatureCrop(block)) {
            if (!event.getPlayer().isSneaking()) {
                event.setCancelled(true);
            }
            return;
        }

        final Player player = event.getPlayer();
        final Material cropMaterial = block.getType();
        final Material plantingMaterial = versionAdapter.resolvePlantingMaterial(cropMaterial);
        final ItemStack offhand = player.getInventory().getItemInOffHand();
        if (plantingMaterial == null || offhand == null || offhand.getType() != plantingMaterial || offhand.getAmount() <= 0) {
            return;
        }

        scheduler.runRegion(block.getLocation(), new Runnable() {
            @Override
            public void run() {
                if (block.getType() != Material.AIR) {
                    return;
                }

                ItemStack currentOffhand = player.getInventory().getItemInOffHand();
                if (currentOffhand == null || currentOffhand.getType() != plantingMaterial || currentOffhand.getAmount() <= 0) {
                    return;
                }

                versionAdapter.replant(block, cropMaterial);
                currentOffhand.setAmount(currentOffhand.getAmount() - 1);
                if (currentOffhand.getAmount() <= 0) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().setItemInOffHand(currentOffhand);
                }
            }
        });
    }
}
