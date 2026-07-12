package com.autoplanting.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface PlatformScheduler {
    void runGlobal(Runnable task);

    void runAsync(Runnable task);

    void runRegion(Location location, Runnable task);

    void runEntity(Entity entity, Runnable task);
}
