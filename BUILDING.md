# Building

## Required JDKs

- `C:\code\path\jdk8`
- `C:\code\path\jdk17`
- `C:\code\path\jdk21`
- `C:\code\path\jdk26`

## Gradle

If the wrapper is missing, generate it once with:

```powershell
C:\code\path\gradle-9.6.1\bin\gradle.bat wrapper
```

Then build with:

```powershell
$env:JAVA_HOME = "C:\code\path\jdk26"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

.\gradlew.bat clean releaseBuild
```

## Release build

The root `releaseBuild` task:

1. Cleans previous artifacts
2. Builds legacy, modern, and current distributions
3. Creates `release/`
4. Copies only final JARs into `release/`

## Output files

- `release/AutoPlanting-2.0.0-legacy.jar`
- `release/AutoPlanting-2.0.0-modern.jar`
- `release/AutoPlanting-2.0.0-current.jar`

## Support buckets

| Bucket | Minecraft versions | Runtimes | Notes |
| --- | --- | --- | --- |
| Legacy | `1.12.x ~ 1.16.5` | CraftBukkit, Spigot, Paper | Use for older servers that still need pre-1.17 compatibility. |
| Modern | `1.17.x ~ 1.21.x` | CraftBukkit, Spigot, Paper | Shared non-Folia build for the 1.17+ line. |
| Current | `26.1.x` | CraftBukkit, Spigot, Paper, Folia | Folia-enabled build. Use this only on the current server generation. |

## Folia support

Only the current distribution declares `folia-supported: true`.
Scheduler dispatch is selected at runtime with platform detection and isolated Folia implementation loading.

## Which jar to use

- `1.12.x ~ 1.16.5` on CraftBukkit / Spigot / Paper:
  - `release/AutoPlanting-2.0.0-legacy.jar`
- `1.17.x ~ 1.21.x` on CraftBukkit / Spigot / Paper:
  - `release/AutoPlanting-2.0.0-modern.jar`
- `26.1.x` on CraftBukkit / Spigot / Paper / Folia:
  - `release/AutoPlanting-2.0.0-current.jar`

## Platform selection

- Use the `Legacy` jar when you want the broadest backward compatibility.
- Use the `Modern` jar when you target the 1.17+ Paper/Bukkit line and do not need Folia.
- Use the `Current` jar when you are on the current Folia-capable generation.

## Adding adapters

- Add a new `VersionAdapter` implementation under `plugin-common`
- Add a new platform adapter under `platform-*`
- Wire it through `PlatformFactory`
