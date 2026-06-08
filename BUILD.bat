@echo off
REM Shop Scraper Mod Build Script
REM Run this file to build the mod

echo ========================================
echo Shop Scraper Mod - Build Script
echo ========================================
echo.

cd /d C:\Users\arewe\minecraft-mods\shop-scraper

if not exist gradlew.bat (
    echo ERROR: gradlew.bat not found!
    echo Make sure you're in the shop-scraper directory
    pause
    exit /b 1
)

echo Building Shop Scraper Mod...
echo.

REM Clean previous build
call gradlew.bat clean

REM Build the mod
call gradlew.bat build

echo.
echo ========================================
if exist build\libs\shop-scraper-1.0.0.jar (
    echo BUILD SUCCESSFUL!
    echo.
    echo JAR File Location:
    echo C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
    echo.
    echo Next Steps:
    echo 1. Copy the JAR to .minecraft/mods/
    echo 2. Launch Minecraft with Fabric Loader
    echo 3. Run: /scrape visual
    echo ========================================
) else (
    echo BUILD FAILED!
    echo Check the output above for errors
    echo ========================================
)

pause
