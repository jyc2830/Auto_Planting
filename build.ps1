param(
    [string]$Version
)

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$versions = @(
    [pscustomobject]@{
        Name = 'legacy'
        Label = 'Legacy'
        Task = 'legacyJar'
    },
    [pscustomobject]@{
        Name = 'modern'
        Label = 'Modern'
        Task = 'modernJar'
    },
    [pscustomobject]@{
        Name = 'current'
        Label = 'Current'
        Task = 'currentJar'
    },
    [pscustomobject]@{
        Name = 'all'
        Label = 'All versions'
        Task = 'releaseBuild'
    }
)

function Invoke-Build {
    param(
        [Parameter(Mandatory = $true)]
        [pscustomobject]$BuildInfo
    )

    Write-Host ""
    Write-Host "==> Building Auto_planting [$($BuildInfo.Label)]"
    $buildDir = ".build-$($BuildInfo.Name)"
    Write-Host "    .\gradlew.bat --no-daemon --project-cache-dir .\.gradle-project-cache -PbuildDir=$buildDir $($BuildInfo.Task)"

    $gradleUserHome = Join-Path $root '.gradle-user-home'
    $gradleProjectCache = Join-Path $root '.gradle-project-cache'
    foreach ($dir in @($gradleUserHome, $gradleProjectCache)) {
        if (-not (Test-Path $dir)) {
            New-Item -ItemType Directory -Path $dir | Out-Null
        }
    }

    $previousGradleUserHome = $env:GRADLE_USER_HOME
    $env:GRADLE_USER_HOME = $gradleUserHome
    try {
        $args = @(
            '--no-daemon',
            '--project-cache-dir', $gradleProjectCache,
            "-PbuildDir=$buildDir",
            $BuildInfo.Task
        )
        & .\gradlew.bat @args
        if ($LASTEXITCODE -ne 0) {
            if ($LASTEXITCODE -eq 1) {
                Write-Host "Gradle lock detected, stopping daemons and retrying once..."
                & .\gradlew.bat --stop | Out-Host
                & .\gradlew.bat @args
            }
            if ($LASTEXITCODE -ne 0) {
                throw "Build failed for Auto_planting [$($BuildInfo.Label)]."
            }
        }
    }
    finally {
        $env:GRADLE_USER_HOME = $previousGradleUserHome
    }

    Write-Host ""
    if ($BuildInfo.Name -eq 'all') {
        $releaseDir = Join-Path $root $buildDir 'release'
        Write-Host "Output files:"
        Get-ChildItem -Path $releaseDir -File -Filter '*.jar' | Sort-Object Name | ForEach-Object {
            Write-Host "    $($_.FullName)"
        }
    }
    else {
        $artifactName = "AutoPlanting-2.0.0-$($BuildInfo.Name).jar"
        $artifactPath = Join-Path (Join-Path $root $buildDir) (Join-Path 'release' $artifactName)
        Write-Host "Output file:"
        Write-Host "    $artifactPath"
    }
}

function Select-Version {
    param(
        [Parameter(Mandatory = $true)]
        [array]$Options
    )

    Write-Host "Select a version to build:"
    for ($i = 0; $i -lt $Options.Count; $i++) {
        Write-Host ("[{0}] {1}" -f ($i + 1), $Options[$i].Label)
    }
    Write-Host ("[{0}] Exit" -f ($Options.Count + 1))

    while ($true) {
        $choice = Read-Host "Enter number"
        $parsed = 0
        if ([int]::TryParse($choice, [ref]$parsed)) {
            if ($parsed -ge 1 -and $parsed -le $Options.Count) {
                return $Options[$parsed - 1]
            }
            if ($parsed -eq $Options.Count + 1) {
                return $null
            }
        }
        Write-Host "Invalid selection. Try again."
    }
}

if ($Version) {
    $selected = @($versions | Where-Object { $_.Name -ieq $Version })
    if (-not $selected) {
        throw "Unknown version '$Version'. Available: $($versions.Name -join ', ')"
    }
} else {
    $selected = @(Select-Version -Options $versions)
}

if (-not $selected) {
    Write-Host "No build selected."
    exit 0
}

Invoke-Build -BuildInfo ($selected[0])
