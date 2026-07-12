package com.autoplanting.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public final class ReplantTaskRunner {
    private final Plugin plugin;
    private final boolean foliaRuntime;

    public ReplantTaskRunner(Plugin plugin) {
        this.plugin = plugin;
        this.foliaRuntime = detectFoliaRuntime();
    }

    public void replantLater(Player player, Location location, Material cropType, Material plantingMaterial) {
        if (player == null || location == null || cropType == null || plantingMaterial == null) {
            return;
        }

        Runnable task = () -> {
            Block block = location.getBlock();
            if (!block.getType().isAir()) {
                return;
            }

            ItemStack offhand = player.getInventory().getItemInOffHand();
            if (offhand.getType() != plantingMaterial || offhand.getAmount() <= 0) {
                return;
            }

            BlockData replanted = createReplantedData(cropType);
            if (replanted == null) {
                return;
            }

            block.setBlockData(replanted, false);
            offhand.setAmount(offhand.getAmount() - 1);
            if (offhand.getAmount() <= 0) {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().setItemInOffHand(offhand);
            }
        };

        if (foliaRuntime) {
            runOnRegionThread(location, task);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, task);
    }

    private boolean detectFoliaRuntime() {
        try {
            Bukkit.class.getMethod("getRegionScheduler");
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    private void runOnRegionThread(Location location, Runnable task) {
        try {
            Method getRegionScheduler = Bukkit.class.getMethod("getRegionScheduler");
            Object regionScheduler = getRegionScheduler.invoke(null);
            Method runMethod = regionScheduler.getClass().getMethod("run", Plugin.class, Location.class, Consumer.class);
            Consumer<Object> consumer = ignored -> task.run();
            runMethod.invoke(regionScheduler, plugin, location, consumer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            plugin.getLogger().warning("Folia region scheduler failed, falling back to Bukkit scheduler: " + ex.getMessage());
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    private BlockData createReplantedData(Material cropType) {
        BlockData blockData = Bukkit.createBlockData(cropType);
        if (blockData instanceof Ageable ageable) {
            ageable.setAge(0);
            return ageable;
        }
        return null;
    }
}
