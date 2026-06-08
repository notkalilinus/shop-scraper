# Implementation Details

## Architecture Overview

### State Machine
The `ScrapeStateMachine` tracks navigation through the shop hierarchy:

```
IDLE → OPENING_SHOP → SCANNING_MAIN → OPENING_CATEGORY 
  ↓                                         ↓
  ← ← ← ← ← ← ← ← ← ← SCANNING_CATEGORY → NEXT_PAGE
```

### Components

#### ActionQueue
- Rate-limited task executor
- Respects configurable delay (default 250ms)
- Prevents anti-cheat kicks
- Executes on `ClientTickEvents.END_CLIENT_TICK`

#### LoreParser
- Extracts lore from item metadata (DataComponentTypes.LORE)
- Uses regex patterns to find Buy/Sell prices
- Handles color codes and formatting
- Supports various price formats (1000, 1K, 1.5M, etc.)

#### CSVWriter
- Appends to `.minecraft/config/shop_prices.csv`
- Auto-initializes with header row
- Sanitizes item names to prevent CSV format issues
- Thread-safe file operations

#### VisualScraper
- Navigates using `MinecraftClient.interactionManager.clickSlot`
- Renders GUI while operating
- Detects categories, items, and navigation buttons
- Handles pagination

#### PacketScraper
- Sends `ClickSlotC2SPacket` directly
- Doesn't render GUI
- Intercepts packets via Mixin
- Faster and less detectable

#### Mixins
- `ClientPlayNetworkHandlerMixin` hooks packet methods
- Intercepts `onOpenScreen`, `onInventory`, `onScreenHandlerSlotUpdate`
- Passes packet data to `PacketScraper`

## Key Implementation Decisions

1. **State Machine**: Handles recursive navigation without nested loops
2. **Rate Limiting**: Prevents anti-cheat detection with configurable delays
3. **Dual Modes**: Visual for debugging, Packet for efficiency
4. **Lore Parsing**: Regex-based to handle various server formats
5. **CSV Format**: Simple, readable, easily imported to spreadsheets
6. **Configuration**: File-based for persistence across sessions

## Extension Points

### Add New Price Patterns
Edit `LoreParser.java` regex patterns:
```java
private static final Pattern CUSTOM_PATTERN = Pattern.compile(
    "your regex here",
    Pattern.CASE_INSENSITIVE
);
```

### Add New Commands
Extend `ScrapeCommand.register()`:
```java
dispatcher.register(literal("scrape")
    .then(literal("yourcommand")
        .executes(ctx -> { /* your logic */ }))
);
```

### Add New Scrapers
Implement similar to `VisualScraper` or `PacketScraper`:
```java
public class NewScraper {
    private ScrapeStateMachine stateMachine;
    public void start() { /* init logic */ }
    public void tick() { /* per-tick logic */ }
}
```

## Performance Considerations

- **Memory**: State machine stores all items in memory (consider chunking for large shops)
- **Network**: Packet mode is network-friendly, only sends clicks
- **CPU**: Regex compilation is cached, minimal CPU overhead
- **Disk**: CSV appends are buffered, minimal I/O

## Known Limitations

1. **Category Detection**: Heuristic-based, may need tuning per server
2. **Price Format**: Regex may not catch all price formats
3. **Pagination**: Assumes "→" symbol for next page
4. **Back Navigation**: Relies on re-opening shop
5. **Concurrent Scraping**: Not designed for parallel scraping

## Testing

1. Create test shop with various item formats
2. Run `/scrape visual` and verify GUI interaction
3. Run `/scrape packet` and verify silent operation
4. Check `.minecraft/config/shop_prices.csv` output
5. Verify prices match expected values
6. Test anti-cheat delays with various settings
