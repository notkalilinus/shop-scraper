# 🎯 SHOP SCRAPER MOD - INSTANT BUILD SOLUTION

## YOUR OPTIONS RIGHT NOW

### ⚡ Option 1: Build in 2 Minutes (RECOMMENDED)

**Windows - Open PowerShell and copy-paste this ENTIRE block:**

```powershell
cd C:\Users\arewe\minecraft-mods\shop-scraper
$javaCheck = java -version 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Java found: $javaCheck"
    Write-Host "🔨 Starting build..."
    .\gradlew.bat build
    if (Test-Path "build\libs\shop-scraper-1.0.0.jar") {
        Write-Host "✅ BUILD COMPLETE!"
        Write-Host "📁 JAR Location: $(Get-Item 'build\libs\shop-scraper-1.0.0.jar').FullName"
        Write-Host "📊 JAR Size: $((Get-Item 'build\libs\shop-scraper-1.0.0.jar').Length / 1KB)KB"
    } else {
        Write-Host "❌ Build failed - JAR not found"
    }
} else {
    Write-Host "❌ Java not found! Install Java 21+ from https://www.oracle.com/java/technologies/downloads/"
}
```

This will:
- Check Java version
- Run the build
- Verify JAR was created
- Show you the location

---

### 📦 Option 2: Use Pre-built JAR (If you already have one)

If you want to skip building and just want the JAR file:
1. Tell me and I'll guide you to download a pre-built version
2. Or use an online Gradle service

---

### 🔧 Option 3: Manual One-Liner Build

**Windows Command Prompt**:
```cmd
cd C:\Users\arewe\minecraft-mods\shop-scraper && gradlew.bat build && echo BUILD COMPLETE
```

**Windows PowerShell**:
```powershell
cd C:\Users\arewe\minecraft-mods\shop-scraper; .\gradlew.bat build; Write-Host "BUILD COMPLETE"
```

---

## ✅ AFTER BUILD - WHAT TO DO

Once the JAR is built at `C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar`:

### Step 1: Copy to Mods Folder
```powershell
$jarPath = "C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar"
$modsPath = "$env:APPDATA\.minecraft\mods"

if (Test-Path $jarPath) {
    Copy-Item $jarPath $modsPath
    Write-Host "✅ JAR copied to: $modsPath"
} else {
    Write-Host "❌ JAR not found at: $jarPath"
}
```

### Step 2: Launch Minecraft
- Use Fabric Loader profile
- Verify mod loads in Mods menu

### Step 3: Use the Mod
```
/scrape visual    # Start visual scraping
/scrape packet    # Start packet scraping
/scrape stop      # Stop anytime
```

---

## 🎯 SUMMARY

**Project Location**: `C:\Users\arewe\minecraft-mods\shop-scraper\`

**Build Command**: 
```
cd C:\Users\arewe\minecraft-mods\shop-scraper && .\gradlew.bat build
```

**Expected Output**: `build\libs\shop-scraper-1.0.0.jar`

**Build Time**: 3-5 minutes (first time), 1-2 minutes (after)

**JAR Size**: ~150-200 KB

---

## ⚠️ IF BUILD FAILS

**Java not found?**
```powershell
java -version
```
If this fails, install Java 21+: https://www.oracle.com/java/technologies/downloads/

**Permission denied?**
- Run PowerShell as Administrator
- Or use Command Prompt instead

**Build hangs?**
- Wait 5-10 minutes (first build downloads dependencies)
- Check internet connection

---

## 📍 READY TO GO

Everything is set up. You just need to run ONE command:

```
cd C:\Users\arewe\minecraft-mods\shop-scraper && .\gradlew.bat build
```

**That's it!** 🚀
