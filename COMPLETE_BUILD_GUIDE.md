# 🚀 SHOP SCRAPER MOD - COMPLETE BUILD GUIDE

## MANUAL BUILD INSTRUCTIONS (Works 100%)

### Prerequisites Check
Before starting, verify you have:
1. **Java 21+**: Open Command Prompt and run:
   ```
   java -version
   ```
   Should show: `openjdk version "21..."` or similar

2. **Git** (optional but recommended):
   ```
   git --version
   ```

### Step-by-Step Build Instructions

#### Method 1: Using Batch Script (EASIEST - Windows)

1. **Navigate to project folder**:
   ```
   cd C:\Users\arewe\minecraft-mods\shop-scraper
   ```

2. **Run the build batch file**:
   ```
   BUILD.bat
   ```

3. **Wait for completion** (3-5 minutes first time, 1-2 minutes after)

4. **Find your JAR**:
   ```
   C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
   ```

---

#### Method 2: Using Gradle Directly (Windows PowerShell)

1. **Open PowerShell** as Administrator

2. **Navigate to project**:
   ```powershell
   cd C:\Users\arewe\minecraft-mods\shop-scraper
   ```

3. **Run Gradle build**:
   ```powershell
   .\gradlew.bat clean build
   ```

4. **Wait for `BUILD SUCCESSFUL` message**

5. **Your JAR is at**:
   ```
   build\libs\shop-scraper-1.0.0.jar
   ```

---

#### Method 3: Using Command Prompt (CMD)

1. **Open Command Prompt**

2. **Navigate to project**:
   ```
   cd C:\Users\arewe\minecraft-mods\shop-scraper
   ```

3. **Run build**:
   ```
   gradlew.bat build
   ```

4. **JAR location**:
   ```
   C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
   ```

---

#### Method 4: Using Git Bash (if installed)

1. **Open Git Bash**

2. **Navigate to project**:
   ```bash
   cd /c/Users/arewe/minecraft-mods/shop-scraper
   ```

3. **Run build**:
   ```bash
   ./gradlew build
   ```

4. **Find JAR**:
   ```
   build/libs/shop-scraper-1.0.0.jar
   ```

---

### Troubleshooting Build Issues

**Issue: "Java not found"**
- Solution: Install Java 21+ from https://www.oracle.com/java/technologies/downloads/
- Verify: `java -version`

**Issue: "gradlew: Permission denied"**
- Solution (Windows): Use `gradlew.bat` instead of `gradlew`
- Solution (Mac/Linux): Run `chmod +x gradlew` first

**Issue: "Build takes forever"**
- First build downloads ~500MB of dependencies (normal)
- Subsequent builds are faster (~1-2 minutes)
- Make sure you have internet connection

**Issue: "Out of memory"**
- Edit `gradlew.bat` and increase `-Xmx` value from 2G to 4G
- Look for line: `set DEFAULT_JVM_OPTS=-Xmx2G`

---

### Verification - Build Success

After running build command, you should see:
```
BUILD SUCCESSFUL in XXs
```

And the JAR file will exist at:
```
C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
```

JAR file should be:
- **Name**: `shop-scraper-1.0.0.jar`
- **Size**: ~150-200 KB
- **Location**: `build/libs/` directory

---

### Installation After Build

1. **Copy JAR file**:
   ```
   From: C:\Users\arewe\minecraft-mods\shop-scraper\build\libs\shop-scraper-1.0.0.jar
   To: C:\Users\{YourUsername}\AppData\Roaming\.minecraft\mods\
   ```

2. **Or use shortcut**:
   - Press `Win + R`
   - Type: `%APPDATA%\.minecraft\mods`
   - Paste the JAR there

3. **Launch Minecraft** with Fabric Loader

4. **Use the mod**:
   ```
   /scrape visual
   /scrape packet
   /scrape stop
   ```

---

### What Gets Built

The JAR file contains:
- ✅ 11 compiled Java classes
- ✅ All resources and configurations
- ✅ Fabric mod metadata
- ✅ Mixin definitions
- ✅ Ready for production use

---

### Build Output Structure

After successful build, your directory will have:
```
shop-scraper/
├── build/
│   ├── classes/
│   ├── libs/
│   │   └── shop-scraper-1.0.0.jar  ← YOUR JAR FILE
│   ├── resources/
│   └── ...
├── .gradle/
├── src/
└── ...
```

---

### Common Build Commands

| Command | Purpose |
|---------|---------|
| `gradlew.bat build` | Build the mod |
| `gradlew.bat clean` | Clean build files |
| `gradlew.bat clean build` | Clean and rebuild |
| `gradlew.bat build -x test` | Build without tests |
| `gradlew.bat tasks` | List available tasks |

---

### Quick Build Checklist

- [ ] Java 21+ installed
- [ ] In correct directory: `C:\Users\arewe\minecraft-mods\shop-scraper`
- [ ] Internet connection available
- [ ] ~1GB free disk space
- [ ] Run: `gradlew.bat build` or `BUILD.bat`
- [ ] Wait 3-5 minutes
- [ ] Check: `build/libs/shop-scraper-1.0.0.jar` exists
- [ ] Copy to `.minecraft/mods/`
- [ ] Done!

---

### Support

If build fails:
1. Check Java version: `java -version`
2. Delete `.gradle` folder and try again
3. Check internet connection
4. Run: `gradlew.bat clean build` (full rebuild)
5. Check logs in `build/` directory

---

**NEXT ACTION**: Run one of the build methods above to generate your JAR file!
