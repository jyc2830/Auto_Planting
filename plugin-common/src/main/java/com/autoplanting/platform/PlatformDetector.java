package com.autoplanting.platform;

public final class PlatformDetector {
    private PlatformDetector() {
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
