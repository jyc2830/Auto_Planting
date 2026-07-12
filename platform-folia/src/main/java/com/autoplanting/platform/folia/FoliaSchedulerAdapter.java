package com.autoplanting.platform.folia;

import com.autoplanting.api.PlatformScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public final class FoliaSchedulerAdapter implements PlatformScheduler {
    private final Plugin plugin;

    public FoliaSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runGlobal(Runnable task) {
        invokeScheduler("getGlobalRegionScheduler", task, null, null);
    }

    @Override
    public void runAsync(Runnable task) {
        invokeScheduler("getAsyncScheduler", task, null, null);
    }

    @Override
    public void runRegion(Location location, Runnable task) {
        invokeScheduler("getRegionScheduler", task, location, null);
    }

    @Override
    public void runEntity(Entity entity, Runnable task) {
        invokeScheduler("getScheduler", task, null, entity);
    }

    private void invokeScheduler(String methodName, Runnable task, Location location, Entity entity) {
        try {
            Object scheduler = methodName.equals("getScheduler")
                ? entity.getClass().getMethod(methodName).invoke(entity)
                : plugin.getServer().getClass().getMethod(methodName).invoke(plugin.getServer());
            for (String runMethodName : new String[]{"execute", "run"}) {
                try {
                    Method runMethod;
                    if (location != null) {
                        runMethod = scheduler.getClass().getMethod(runMethodName, Plugin.class, Location.class, Runnable.class);
                        runMethod.invoke(scheduler, plugin, location, task);
                        return;
                    }
                    runMethod = scheduler.getClass().getMethod(runMethodName, Plugin.class, Runnable.class);
                    runMethod.invoke(scheduler, plugin, task);
                    return;
                } catch (NoSuchMethodException ignored) {
                }
            }
        } catch (Throwable ignored) {
            task.run();
        }
    }
}
