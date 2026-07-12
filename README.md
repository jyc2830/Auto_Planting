# Auto Planting

Minecraft auto-replant plugin rebuilt as a Gradle multi-project build.

## Supported releases

| Release bucket | Supported Minecraft versions | Supported runtimes | Jar |
| --- | --- | --- | --- |
| Legacy | `1.12.x ~ 1.16.5` | CraftBukkit, Spigot, Paper | `AutoPlanting-2.0.0-legacy.jar` |
| Modern | `1.17.x ~ 1.21.x` | CraftBukkit, Spigot, Paper | `AutoPlanting-2.0.0-modern.jar` |
| Current | `26.1.x` | CraftBukkit, Spigot, Paper, Folia | `AutoPlanting-2.0.0-current.jar` |

## Version Notes

- `Legacy` is the compatibility bucket for older servers that still expect pre-1.17 behavior.
- `Modern` is the shared bucket for the 1.17+ Paper/Bukkit line before the current Folia-targeted build.
- `Current` is the only release line that declares `folia-supported: true`.
- Do not use the `Current` jar on a non-matching older server family unless you have verified compatibility in that server version.

## Platforms

- CraftBukkit
- Spigot
- Paper
- Folia

## Build

```powershell
$env:JAVA_HOME = "C:\code\path\jdk26"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

C:\code\path\gradle-9.6.1\bin\gradle.bat clean releaseBuild
```

## Output

- `release/AutoPlanting-2.0.0-legacy.jar`
- `release/AutoPlanting-2.0.0-modern.jar`
- `release/AutoPlanting-2.0.0-current.jar`
