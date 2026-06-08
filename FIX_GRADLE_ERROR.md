# 🔧 FIX: Missing Gradle Wrapper JAR

## Problem
```
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
```

This means the `gradle-wrapper.jar` file is missing.

---

## ✅ Solution - Try This First

### Run the Fix Script

Double-click this file in your project folder:
```
FIX_AND_BUILD.bat
```

This will:
1. Download the missing gradle-wrapper.jar
2. Place it in the correct location
3. Run the build automatically
4. Show you the JAR location

**That's it!** ⏱️ Wait 5-10 minutes.

---

## 🔗 Manual Download (If Auto-Fix Doesn't Work)

### Step 1: Download the JAR File

Download from this link:
```
https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar
```

Or use this direct link:
```
https://gradle-org.s3.amazonaws.com/gradle/gradle-8.10-wrapper.jar
```

### Step 2: Place in Correct Location

1. Create folder (if not exists):
   ```
   C:\Users\arewe\minecraft-mods\shop-scraper\gradle\wrapper\
   ```

2. Copy the downloaded file into that folder

3. Rename to: `gradle-wrapper.jar`

### Step 3: Run Build

Open PowerShell in the project folder and run:
```powershell
cd C:\Users\arewe\minecraft-mods\shop-scraper
.\gradlew.bat build
```

---

## 📁 Final Structure Should Look Like

```
shop-scraper/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar          ← This was missing!
│       └── gradle-wrapper.properties
├── src/
├── build.gradle
├── gradlew.bat
├── BUILD.bat
└── FIX_AND_BUILD.bat
```

---

## 🚀 Quick Fix Commands

**PowerShell** (paste this entire block):

```powershell
$project = "C:\Users\arewe\minecraft-mods\shop-scraper"
$wrapperDir = "$project\gradle\wrapper"
$jarUrl = "https://github.com/gradle/gradle/releases/download/v8.10.0/gradle-8.10-wrapper.jar"
$jarPath = "$wrapperDir\gradle-wrapper.jar"

# Create directory
New-Item -ItemType Directory -Force -Path $wrapperDir | Out-Null

# Download
Write-Host "Downloading gradle-wrapper.jar..."
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
(New-Object Net.WebClient).DownloadFile($jarUrl, $jarPath)

# Verify
if (Test-Path $jarPath) {
    Write-Host "✅ Downloaded successfully!"
    Write-Host "📁 Location: $jarPath"
    Write-Host "📊 Size: $((Get-Item $jarPath).Length / 1MB)MB"
} else {
    Write-Host "❌ Download failed"
}
```

---

## ⚡ After Fix is Applied

Once the JAR is in place, run:

```powershell
cd C:\Users\arewe\minecraft-mods\shop-scraper
.\gradlew.bat build
```

This should now work and show:
```
BUILD SUCCESSFUL in 2m 45s
```

---

## 📍 Your JAR Will Be At

```
C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
```

---

## 🎯 Next Steps

1. **Try**: Double-click `FIX_AND_BUILD.bat`
2. **Wait**: 5-10 minutes
3. **Check**: Look for `BUILD SUCCESSFUL` message
4. **Verify**: JAR exists at `build/libs/shop-scraper-1.0.0.jar`
5. **Copy**: To `.minecraft/mods/`
6. **Play**: Launch Minecraft!

---

**The fix is ready! Try FIX_AND_BUILD.bat now! 🚀**
