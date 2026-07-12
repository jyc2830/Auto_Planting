package com.autoplanting.platform;

import com.autoplanting.api.PlatformScheduler;
import com.autoplanting.api.VersionAdapter;
import com.autoplanting.platform.bukkit.BukkitSchedulerAdapter;
import com.autoplanting.platform.version.ReflectionVersionAdapter;
import org.bukkit.plugin.Plugin;

public final class PlatformFactory {
    private PlatformFactory() {
    }

    public static PlatformScheduler createScheduler(Plugin plugin) {
        if (PlatformDetector.isFolia()) {
            try {
                Class<?> type = Class.forName("com.autoplanting.platform.folia.FoliaSchedulerAdapter");
                return (PlatformScheduler) type.getConstructor(Plugin.class).newInstance(plugin);
            } catch (ReflectiveOperationException ex) {
                return new BukkitSchedulerAdapter(plugin);
            }
        }
        return new BukkitSchedulerAdapter(plugin);
    }

    public static VersionAdapter createVersionAdapter() {
        return new ReflectionVersionAdapter();
    }
}
