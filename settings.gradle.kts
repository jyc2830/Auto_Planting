pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

rootProject.name = "Auto_Planting"

include(
    "plugin-api",
    "plugin-common",
    "platform-bukkit",
    "platform-paper",
    "platform-folia",
    "version-legacy",
    "version-modern",
    "version-current",
    "distribution-legacy",
    "distribution-modern",
    "distribution-current",
)
