package com.autoplanting.platform.paper;

import com.autoplanting.api.PlatformScheduler;
import com.autoplanting.platform.bukkit.BukkitSchedulerAdapter;
import org.bukkit.plugin.Plugin;

public final class PaperSchedulerAdapter extends BukkitSchedulerAdapter implements PlatformScheduler {
    public PaperSchedulerAdapter(Plugin plugin) {
        super(plugin);
    }
}
