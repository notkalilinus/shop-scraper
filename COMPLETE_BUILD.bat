@echo off
REM Direct Gradle Wrapper JAR Download and Build Script
REM This script downloads the gradle wrapper and builds the mod

setlocal enabledelayedexpansion

echo.
echo ========================================
echo Shop Scraper Mod - Complete Build
echo ========================================
echo.

cd /d C:\Users\arewe\minecraft-mods\shop-scraper

REM Check if gradle/wrapper directory exists
if not exist "gradle\wrapper" (
    echo Creating gradle\wrapper directory...
    mkdir gradle\wrapper
    echo ✅ Directory created
)

REM Check if gradle-wrapper.jar exists
if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ✅ gradle-wrapper.jar already exists
    goto SKIP_DOWNLOAD
)

echo.
echo Downloading gradle-wrapper.jar...
echo This may take 1-2 minutes on first run...
echo.

REM Use PowerShell to download with progress
powershell -NoProfile -Command "^
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; ^
    $url = 'https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar'; ^
    $file = 'gradle\wrapper\gradle-wrapper.jar'; ^
    try { ^
        Write-Host 'Downloading from: ' $url; ^
        $client = New-Object System.Net.WebClient; ^
        $client.DownloadFile($url, $file); ^
        if (Test-Path $file) { ^
            $size = (Get-Item $file).Length / 1MB; ^
            Write-Host \"✅ Downloaded successfully! Size: $($size)MB\"; ^
            exit 0; ^
        } else { ^
            Write-Host '❌ File not created'; ^
            exit 1; ^
        } ^
    } catch { ^
        Write-Host \"❌ Download failed: $($_.Exception.Message)\"; ^
        exit 1; ^
    } ^
"

if !ERRORLEVEL! NEQ 0 (
    echo.
    echo ❌ Automatic download failed
    echo.
    echo Please manually download from:
    echo https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar
    echo.
    echo And save to:
    echo C:\Users\arewe\minecraft-mods\shop-scraper\gradle\wrapper\gradle-wrapper.jar
    echo.
    pause
    exit /b 1
)

:SKIP_DOWNLOAD
echo.
echo ✅ Gradle wrapper ready
echo.
echo ========================================
echo Building Shop Scraper Mod...
echo ========================================
echo.

REM Run clean build
call gradlew.bat clean build

echo.
echo ========================================

if exist "build\libs\shop-scraper-1.0.0.jar" (
    echo ✅ BUILD SUCCESSFUL!
    echo.
    echo 📁 Your JAR file is at:
    echo C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
    echo.
    echo 📊 File size:
    for /f "usebackq" %%A in (`powershell -Command "'{0:N2} MB' -f ((Get-Item 'build\libs\shop-scraper-1.0.0.jar').Length / 1MB)"`) do (
        echo %%A
    )
    echo.
    echo 🎮 Next steps:
    echo 1. Copy JAR to: %%APPDATA%%\.minecraft\mods\
    echo 2. Launch Minecraft with Fabric Loader
    echo 3. Run: /scrape visual
    echo.
    echo ========================================
) else (
    echo ❌ BUILD FAILED
    echo.
    echo The JAR file was not created.
    echo Check the error messages above.
    echo.
    echo Troubleshooting:
    echo - Verify Java 21+ is installed
    echo - Check internet connection
    echo - Try: gradlew.bat clean build (manually)
    echo.
    echo ========================================
)

pause
