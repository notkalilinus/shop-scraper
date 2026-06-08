# API Reference

## Commands

### /scrape visual
Starts visual mode scraping.
- Shows GUI as it navigates
- Uses mouse clicks
- Useful for debugging

```
/scrape visual
```

### /scrape packet
Starts packet mode scraping.
- Silent, no GUI rendering
- Uses network packets
- Faster execution

```
/scrape packet
```

### /scrape stop
Stops the current scraping session.
- Clears action queue
- Resets state machine
- Safe to use anytime

```
/scrape stop
```

### /scrape status
Shows current scraping status.
- Displays running/idle state
- Shows queue size
- Shows CSV file path

```
/scrape status
```

### /scrape config delay <milliseconds>
Sets the delay between actions.
- Default: 250ms
- Min: 50ms (risky, may trigger anti-cheat)
- Max: 5000ms (very slow)

```
/scrape config delay 500
```

### /scrape config debug
Toggles debug logging.
- Shows detailed state machine updates
- Shows packet interceptions
- Useful for troubleshooting

```
/scrape config debug
```

## Configuration File

Location: `.minecraft/config/shop-scraper.properties`

### Properties

**action_delay_ms**
- Type: Integer
- Default: 250
- Unit: milliseconds
- Effect: Delay between GUI/packet actions

**debug_mode**
- Type: Boolean
- Default: false
- Effect: Enable verbose logging

## CSV Output Format

Location: `.minecraft/config/shop_prices.csv`

### Columns
1. **Item**: Item name (sanitized)
2. **Category**: Category name
3. **Buy Price**: Extracted buy price
4. **Sell Price**: Extracted sell price

### Example
```csv
Item,Category,Buy Price,Sell Price
Diamond Pickaxe,Tools,6400,3200
Emerald,Materials,1,0.5
Enchanted Book,Books,500,250
```

## Java API

### ScrapeStateMachine
Main state tracking class.

```java
ScrapeStateMachine machine = new ScrapeStateMachine();

// Push a category to visit
machine.pushCategory("Tools");

// Get current state
ScrapeStateMachine.State state = machine.getCurrentState();

// Add scraped item
machine.addItem(new ScrapeStateMachine.ScrapedItem(
    "Diamond Pickaxe", "Tools", "6400", "3200"
));

// Get collected items
Collection<ScrapeStateMachine.ScrapedItem> items = machine.getCollectedItems();

// Reset for new session
machine.reset();
```

### LoreParser
Price extraction utility.

```java
ItemStack item = /* ... */;

// Extract all lore lines
List<String> lore = LoreParser.extractLore(item);

// Extract buy price
String buyPrice = LoreParser.extractBuyPrice(lore);

// Extract sell price
String sellPrice = LoreParser.extractSellPrice(lore);

// Extract both at once
LoreParser.PriceData prices = LoreParser.extractPrices(item);
System.out.println(prices.buyPrice);
System.out.println(prices.sellPrice);
```

### CSVWriter
File output utility.

```java
// Initialize CSV file
CSVWriter.initialize();

// Append single item
ScrapeStateMachine.ScrapedItem item = /* ... */;
CSVWriter.appendItem(item);

// Append multiple items
Collection<ScrapeStateMachine.ScrapedItem> items = /* ... */;
CSVWriter.appendItems(items);

// Get file path
Path csvPath = CSVWriter.getCSVPath();
```

### ActionQueue
Rate-limited task executor.

```java
ActionQueue queue = ShopScraperMod.ACTION_QUEUE;

// Enqueue a task
queue.enqueue(() -> {
    System.out.println("This runs after delay");
});

// Activate queue
queue.setActive(true);

// Check if active
boolean active = queue.isActive();

// Get queue size
int size = queue.getQueueSize();

// Clear queue
queue.clear();
```

### ScraperConfig
Configuration management.

```java
ScraperConfig config = ShopScraperMod.CONFIG;

// Load from file
config.load();

// Get delay
int delay = config.getActionDelayMs();

// Set delay
config.setActionDelayMs(500);

// Get debug mode
boolean debug = config.isDebugMode();

// Set debug mode
config.setDebugMode(true);

// Save changes
config.save();
```

## Logging

The mod logs to `com.shopscraper` logger.

### Log Levels

**INFO**
- Mod initialization
- Session start/stop
- Completion messages

**DEBUG** (when debug mode enabled)
- State machine state changes
- Item detection
- Price extraction results

**ERROR**
- Failed operations
- Configuration errors
- File I/O exceptions

### Log File
Location: `.minecraft/logs/latest.log`

Filter by: `[Shop Scraper]`
