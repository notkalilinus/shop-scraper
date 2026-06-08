# Shop Scraper - Quick Start Guide

## Installation (5 minutes)

### Prerequisites
- Java 21+ (verify: `java -version`)
- Minecraft 1.21.1
- Fabric Loader 0.16.9+

### Step 1: Build
```bash
# Windows
cd C:\Users\arewe\minecraft-mods\shop-scraper
gradlew.bat build

# macOS/Linux
cd ~/minecraft-mods/shop-scraper
chmod +x gradlew
./gradlew build
```

### Step 2: Install
1. Copy `build/libs/shop-scraper-1.0.0.jar`
2. Paste to `.minecraft/mods/` folder
3. Launch Minecraft with Fabric Loader

### Step 3: Use
```
/scrape visual    # Start scraping with GUI
/scrape packet    # Start scraping silently
/scrape stop      # Stop scraping
/scrape status    # Check progress
```

## Key Features

✅ **Visual Mode** - See the shopping GUI as it navigates  
✅ **Packet Mode** - Silent scraping without GUI rendering  
✅ **State Machine** - Handles nested menus automatically  
✅ **Lore Parsing** - Extracts prices from item descriptions  
✅ **Anti-Cheat** - Configurable delays (default 250ms)  
✅ **CSV Export** - Clean output format  

## Output Location

```
.minecraft/config/shop_prices.csv
```

Format: `Item,Category,Buy Price,Sell Price`

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Mod not loading | Check Fabric Loader is installed |
| No prices found | Verify shop GUI format, enable debug |
| Too slow/fast | Adjust delay: `/scrape config delay 500` |
| Errors in log | Check Java version (need 21+) |

## Commands

| Command | Purpose |
|---------|---------|
| `/scrape visual` | Visual mode scraping |
| `/scrape packet` | Packet mode scraping |
| `/scrape stop` | Stop current session |
| `/scrape status` | Show status |
| `/scrape config delay <ms>` | Set action delay |
| `/scrape config debug` | Toggle debug logging |

## Configuration

File: `.minecraft/config/shop-scraper.properties`

```properties
action_delay_ms=250       # Delay between actions (milliseconds)
debug_mode=false          # Enable debug logging
```

## How It Works

1. **Command Entered** → `/scrape visual`
2. **State Machine Starts** → Opens shop menu
3. **Scans Main Shop** → Identifies categories
4. **For Each Category** → Enters category, scans items
5. **Extracts Prices** → Uses regex on item lore
6. **Handles Pagination** → Clicks "Next Page" if needed
7. **Saves to CSV** → Appends data to file
8. **Complete** → Returns to idle state

## Visual vs Packet Mode

### Visual Mode
- Shows GUI while navigating
- Uses mouse clicks
- Better for testing/debugging
- Slightly slower

### Packet Mode
- Silent, no GUI rendering
- Uses network packets
- Faster, less detectable
- Requires packet interception

## FAQ

**Q: Is this safe?**  
A: Uses configurable anti-cheat delays. Default 250ms is conservative.

**Q: Can I use on any server?**  
A: Works on any server with /shop command and inventory menus.

**Q: How long does it take?**  
A: Depends on shop size and delay setting. ~50 items/minute with default settings.

**Q: What if the shop format changes?**  
A: Adjust lore patterns in `LoreParser.java` or submit an issue.

## Support

- Check README.md for full documentation
- Enable debug: `/scrape config debug`
- Check `.minecraft/logs/latest.log` for errors
- Verify Java 21+: `java -version`
