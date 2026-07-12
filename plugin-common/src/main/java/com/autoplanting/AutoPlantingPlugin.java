package com.autoplanting;

import com.autoplanting.api.PlatformScheduler;
import com.autoplanting.api.VersionAdapter;
import com.autoplanting.listener.CropHarvestListener;
import com.autoplanting.platform.PlatformFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoPlantingPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        PlatformScheduler scheduler = PlatformFactory.createScheduler(this);
        VersionAdapter versionAdapter = PlatformFactory.createVersionAdapter();
        getServer().getPluginManager().registerEvents(new CropHarvestListener(this, scheduler, versionAdapter), this);
    }
}
