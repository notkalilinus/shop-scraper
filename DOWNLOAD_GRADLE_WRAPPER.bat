@echo off
REM Direct Gradle Wrapper Download - Standalone Script
REM This script downloads gradle-wrapper.jar directly

setlocal enabledelayedexpansion

echo.
echo ========================================
echo Gradle Wrapper JAR Download
echo ========================================
echo.

cd /d C:\Users\arewe\minecraft-mods\shop-scraper

REM Create gradle/wrapper directory
if not exist "gradle\wrapper" (
    echo Creating gradle\wrapper directory...
    mkdir gradle\wrapper
)

echo.
echo Downloading gradle-wrapper.jar from GitHub...
echo.

REM Download using PowerShell with retry logic
powershell -NoProfile -ExecutionPolicy Bypass -Command "^
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; ^
    $url = 'https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar'; ^
    $file = 'gradle\wrapper\gradle-wrapper.jar'; ^
    $retries = 3; ^
    for ($i = 0; $i -lt $retries; $i++) { ^
        try { ^
            Write-Host \"Download attempt $($i+1) of $retries\"; ^
            $ProgressPreference = 'SilentlyContinue'; ^
            Invoke-WebRequest -Uri $url -OutFile $file -TimeoutSec 60; ^
            if (Test-Path $file) { ^
                $size = (Get-Item $file).Length / 1MB; ^
                Write-Host \"✅ Downloaded successfully!\"; ^
                Write-Host \"Size: $($size)MB\"; ^
                exit 0; ^
            } ^
        } catch { ^
            Write-Host \"Attempt $($i+1) failed: $($_.Exception.Message)\"; ^
            if ($i -lt $retries - 1) { Start-Sleep -Seconds 5; } ^
        } ^
    } ^
    Write-Host \"❌ Download failed after $retries attempts\"; ^
    exit 1; ^
"

if !ERRORLEVEL! NEQ 0 (
    echo.
    echo ❌ Download failed. Trying alternate source...
    echo.

    REM Try alternate download
    powershell -NoProfile -ExecutionPolicy Bypass -Command "^
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; ^
        $url = 'https://gradle-org.s3.amazonaws.com/gradle/gradle-8.10-wrapper.jar'; ^
        $file = 'gradle\wrapper\gradle-wrapper.jar'; ^
        try { ^
            Write-Host \"Downloading from S3...\"; ^
            $ProgressPreference = 'SilentlyContinue'; ^
            Invoke-WebRequest -Uri $url -OutFile $file -TimeoutSec 60; ^
            if (Test-Path $file) { ^
                $size = (Get-Item $file).Length / 1MB; ^
                Write-Host \"✅ Downloaded successfully from alternate source!\"; ^
                Write-Host \"Size: $($size)MB\"; ^
                exit 0; ^
            } ^
        } catch { ^
            Write-Host \"❌ Alternate download also failed\"; ^
            exit 1; ^
        } ^
    "

    if !ERRORLEVEL! NEQ 0 (
        echo.
        echo ================================================
        echo MANUAL DOWNLOAD REQUIRED
        echo ================================================
        echo.
        echo The automatic download failed. Please download manually:
        echo.
        echo URL: https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar
        echo.
        echo Save to: C:\Users\arewe\minecraft-mods\shop-scraper\gradle\wrapper\gradle-wrapper.jar
        echo.
        echo Then run BUILD.bat again
        echo.
        echo ================================================
        echo.
        pause
        exit /b 1
    )
)

echo.
echo ✅ gradle-wrapper.jar is ready
echo.
echo Now run: BUILD.bat
echo.
pause
