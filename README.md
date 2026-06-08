# Shop Scraper Mod - Fabric 1.21.1

A powerful client-side Minecraft Fabric mod that scrapes GUI shop prices into CSV format with dual scraping modes.

## Features

- **Visual Mode**: Simulates mouse clicks to navigate shop menus while rendering
- **Packet Mode**: Intercepts network packets to scrape silently without GUI rendering
- **State Machine**: Recursive navigation through nested shop menus (Main → Categories → Items → Pages)
- **Lore Parsing**: Regex-based price extraction from item lore (Buy/Sell/Value patterns)
- **Anti-Cheat**: Configurable delays (default 250ms) between actions to avoid detection
- **CSV Export**: Organized output with Item, Category, Buy Price, Sell Price columns

## Installation

1. Build the mod: `./gradlew build` (or `gradlew.bat build` on Windows)
2. Copy `build/libs/shop-scraper-1.0.0.jar` to `.minecraft/mods/`
3. Launch Minecraft with Fabric Loader 1.21.1

## Usage

### Commands

- `/scrape visual` - Start scraping with visual mode (shows GUI)
- `/scrape packet` - Start scraping with packet mode (silent, no GUI)
- `/scrape stop` - Stop the current scraping session
- `/scrape status` - Show current scraping status
- `/scrape config delay <milliseconds>` - Set delay between actions
- `/scrape config debug` - Toggle debug logging

### Example Workflow

```
/scrape visual          # Start visual scraping
# Wait for completion (check .minecraft/shop_prices.csv)
/scrape stop           # Stop if needed

/scrape packet         # Start silent packet-based scraping
/scrape status         # Check progress
```

## Configuration

Config file: `.minecraft/config/shop-scraper.properties`

```properties
action_delay_ms=250
debug_mode=false
```

## Output

CSV File: `.minecraft/config/shop_prices.csv`

Format: `Item,Category,Buy Price,Sell Price`

Example:
```
Diamond Pickaxe,Tools,6400,3200
Emerald,Materials,1,0.5
Enchanted Book,Books,500,250
```

## Architecture

- **ActionQueue**: Rate-limited task executor with anti-cheat delays
- **ScrapeStateMachine**: Tracks navigation state through nested menus
- **LoreParser**: Regex-based price extraction from item metadata
- **VisualScraper**: GUI-based interaction mode
- **PacketScraper**: Packet-based silent mode
- **CSVWriter**: Organized output to config directory
- **Mixins**: Network packet interception for advanced features

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API 0.102.0+
- Java 21+

## Troubleshooting

**Mod not loading:**
- Verify Fabric Loader is installed
- Check mod JAR is in `.minecraft/mods/`
- Check Java version: `java -version` (need 21+)

**No prices being scraped:**
- Verify shop GUI format matches expected structure
- Enable debug mode: `/scrape config debug`
- Check lore contains Buy/Sell/Value keywords
- Adjust regex patterns if needed

**Scraping too slow/fast:**
- Adjust delay: `/scrape config delay 500` (increase for slower servers)
- Default 250ms balances speed and anti-cheat safety

## Development

- Source: `src/main/java/com/shopscraper/`
- Build: `./gradlew build`
- Run tests: `./gradlew test`

## License

MIT License - See LICENSE file for details
