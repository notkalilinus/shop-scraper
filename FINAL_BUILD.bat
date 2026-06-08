@echo off
REM ========================================
REM Shop Scraper Mod - FINAL BUILD SOLUTION
REM ========================================
REM This script fixes the gradle wrapper and builds the mod

setlocal enabledelayedexpansion

cd /d C:\Users\arewe\minecraft-mods\shop-scraper

echo.
echo ========================================
echo Shop Scraper Mod - Build Solution
echo ========================================
echo.

REM Step 1: Create directories
if not exist "gradle\wrapper" mkdir gradle\wrapper

echo [1/4] Downloading gradle-wrapper.jar...

REM Step 2: Download gradle wrapper JAR
powershell -NoProfile -ExecutionPolicy Bypass -Command "^
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; ^
    $ErrorActionPreference = 'Stop'; ^
    $url = 'https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar'; ^
    $file = 'gradle\wrapper\gradle-wrapper.jar'; ^
    try { ^
        Write-Host 'Downloading from GitHub...'; ^
        (New-Object Net.WebClient).DownloadFile($url, $file); ^
        Write-Host '✅ Download successful'; ^
    } catch { ^
        Write-Host '⚠️ GitHub download failed, trying S3...'; ^
        $url2 = 'https://gradle-org.s3.amazonaws.com/gradle/gradle-8.10-wrapper.jar'; ^
        try { ^
            (New-Object Net.WebClient).DownloadFile($url2, $file); ^
            Write-Host '✅ S3 download successful'; ^
        } catch { ^
            Write-Host '❌ All downloads failed'; ^
            exit 1; ^
        } ^
    } ^
"

if !ERRORLEVEL! NEQ 0 (
    echo.
    echo ❌ Gradle wrapper download failed
    echo Please download manually from:
    echo https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar
    echo.
    echo Save to: gradle\wrapper\gradle-wrapper.jar
    echo.
    pause
    exit /b 1
)

echo [2/4] Verifying gradle-wrapper.jar...

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ❌ Gradle wrapper JAR not found
    pause
    exit /b 1
)

echo ✅ gradle-wrapper.jar verified

echo.
echo [3/4] Cleaning previous builds...
call gradlew.bat clean >nul 2>&1

echo.
echo [4/4] Building Shop Scraper Mod...
echo ========================================
echo.

call gradlew.bat build

echo.
echo ========================================

if exist "build\libs\shop-scraper-1.0.0.jar" (
    echo ✅ BUILD SUCCESSFUL!
    echo.
    for /f "usebackq" %%A in (`powershell -Command "'{0:N2} MB' -f ((Get-Item 'build\libs\shop-scraper-1.0.0.jar').Length / 1MB)"`) do set SIZE=%%A
    echo JAR File: build\libs\shop-scraper-1.0.0.jar
    echo Size: !SIZE!
    echo.
    echo ✅ Your mod is ready!
    echo.
    echo Next steps:
    echo 1. Copy JAR to %%APPDATA%%\.minecraft\mods\
    echo 2. Launch Minecraft with Fabric Loader 1.21.1
    echo 3. Run: /scrape visual
    echo.
) else (
    echo ❌ BUILD FAILED
    echo.
    echo The JAR file was not created.
    echo Please check the build output above for errors.
    echo.
    echo If you see "Java not found", install Java 21+
    echo.
)

echo ========================================
pause
