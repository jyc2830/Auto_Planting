package com.autoplanting;

import com.autoplanting.listener.CropHarvestListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoPlantingPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CropHarvestListener(this), this);
    }
}
