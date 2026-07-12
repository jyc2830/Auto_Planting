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

- Legacy: `1.12.x ~ 1.16.5`
- Modern: `1.17.x ~ 1.21.x`
- Current: `26.1.x`

## Folia support

Only the current distribution declares `folia-supported: true`.
Scheduler dispatch is selected at runtime with platform detection and isolated Folia implementation loading.

## Adding adapters

- Add a new `VersionAdapter` implementation under `plugin-common`
- Add a new platform adapter under `platform-*`
- Wire it through `PlatformFactory`
