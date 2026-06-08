@echo off
REM Download and setup Gradle wrapper

setlocal enabledelayedexpansion

echo ========================================
echo Gradle Wrapper Setup
echo ========================================
echo.

REM Create gradle/wrapper directory if it doesn't exist
if not exist "gradle\wrapper" (
    echo Creating gradle\wrapper directory...
    mkdir gradle\wrapper
)

REM Check if gradle-wrapper.jar already exists
if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ✅ gradle-wrapper.jar already exists
    goto BUILD
)

echo Downloading gradle-wrapper.jar...
echo This may take a minute...
echo.

REM Download the gradle wrapper jar using PowerShell
powershell -Command "^
    $url = 'https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar'; ^
    $output = 'gradle\wrapper\gradle-wrapper.jar'; ^
    try { ^
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; ^
        $webClient = New-Object System.Net.WebClient; ^
        $webClient.DownloadFile($url, $output); ^
        Write-Host '✅ Download complete'; ^
        exit 0; ^
    } catch { ^
        Write-Host '❌ Download failed'; ^
        Write-Host $_.Exception.Message; ^
        exit 1; ^
    } ^
"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Failed to download gradle-wrapper.jar
    echo.
    echo Manual download:
    echo 1. Download from: https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar
    echo 2. Save to: gradle\wrapper\gradle-wrapper.jar
    echo 3. Run BUILD.bat again
    echo.
    pause
    exit /b 1
)

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ❌ JAR file was not created
    pause
    exit /b 1
)

echo ✅ gradle-wrapper.jar is ready
echo.

:BUILD
echo ========================================
echo Building Shop Scraper Mod...
echo ========================================
echo.

REM Run the build
call gradlew.bat build

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo ✅ BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo JAR File Location:
    echo build\libs\shop-scraper-1.0.0.jar
    echo.
    echo Next Steps:
    echo 1. Copy the JAR to .minecraft/mods/
    echo 2. Launch Minecraft with Fabric Loader
    echo 3. Run: /scrape visual
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ BUILD FAILED!
    echo ========================================
    echo.
    echo Check the output above for errors
    echo.
)

pause
