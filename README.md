# Auto Planting

Minecraft auto-replant plugin rebuilt as a Gradle multi-project build.

## Supported releases

- `1.12.x ~ 1.16.5` legacy distribution
- `1.17.x ~ 1.21.x` modern distribution
- `26.1.x` current distribution

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
