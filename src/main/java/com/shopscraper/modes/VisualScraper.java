package com.shopscraper.modes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import com.shopscraper.ShopScraperMod;
import com.shopscraper.core.LoreParser;
import com.shopscraper.core.ScrapeStateMachine;
import com.shopscraper.core.CSVWriter;

public class VisualScraper {
	private final ScrapeStateMachine stateMachine;
	private MinecraftClient client = MinecraftClient.getInstance();
	private static final String NEXT_PAGE_INDICATOR = "→";
	private static final String BACK_INDICATOR = "←";

	public VisualScraper(ScrapeStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public void start() {
		stateMachine.setState(ScrapeStateMachine.State.OPENING_SHOP);
		ShopScraperMod.ACTION_QUEUE.setActive(true);

		// Queue command to open shop
		ShopScraperMod.ACTION_QUEUE.enqueue(() -> {
			client.getNetworkHandler().sendChatCommand("shop");
		});
	}

	public void tick() {
		if (!ShopScraperMod.ACTION_QUEUE.isActive()) {
			return;
		}

		HandledScreen<?> screen = getCurrentScreen();
		if (screen == null) {
			return;
		}

		ScrapeStateMachine.State state = stateMachine.getCurrentState();

		switch (state) {
			case SCANNING_MAIN -> scanMainShop(screen);
			case SCANNING_CATEGORY -> scanCategory(screen);
			case NEXT_PAGE -> handleNextPage(screen);
			default -> {
				// Wait for screen to load
			}
		}
	}

	private void scanMainShop(HandledScreen<?> screen) {
		int itemsFound = 0;

		for (Slot slot : screen.getScreenHandler().slots) {
			ItemStack stack = slot.getStack();
			if (stack.isEmpty()) continue;

			String itemName = stack.getName().getString();

			// Check if this is a category (typically has an arrow or special lore)
			if (isCategory(itemName, stack)) {
				stateMachine.pushCategory(itemName);
				itemsFound++;
			}
		}

		ShopScraperMod.LOGGER.info("Found {} categories", itemsFound);

		if (itemsFound > 0) {
			// Pop first category and open it
			String category = stateMachine.popCategory();
			clickItemByName(screen, category);
			stateMachine.setState(ScrapeStateMachine.State.OPENING_CATEGORY);
		} else {
			stateMachine.setState(ScrapeStateMachine.State.IDLE);
			ShopScraperMod.ACTION_QUEUE.setActive(false);
		}
	}

	private void scanCategory(HandledScreen<?> screen) {
		for (Slot slot : screen.getScreenHandler().slots) {
			ItemStack stack = slot.getStack();
			if (stack.isEmpty()) continue;

			String itemName = stack.getName().getString();

			// Skip category navigation items
			if (isNavigationItem(itemName)) continue;

			LoreParser.PriceData prices = LoreParser.extractPrices(stack);
			if (!prices.buyPrice.isEmpty() || !prices.sellPrice.isEmpty()) {
				ScrapeStateMachine.ScrapedItem item = new ScrapeStateMachine.ScrapedItem(
					itemName,
					stateMachine.getCurrentCategory(),
					prices.buyPrice,
					prices.sellPrice
				);
				stateMachine.addItem(item);
			}
		}

		// Check for "Next Page" button
		boolean hasNextPage = false;
		for (Slot slot : screen.getScreenHandler().slots) {
			ItemStack stack = slot.getStack();
			if (!stack.isEmpty() && stack.getName().getString().contains(NEXT_PAGE_INDICATOR)) {
				hasNextPage = true;
				ShopScraperMod.ACTION_QUEUE.enqueue(() -> clickSlot(screen, slot.getIndex()));
				break;
			}
		}

		if (!hasNextPage) {
			// Look for back button or reopen shop
			stateMachine.resetPage();
			String nextCategory = stateMachine.popCategory();
			if (nextCategory != null) {
				// Reopen shop and select next category
				ShopScraperMod.ACTION_QUEUE.enqueue(() -> {
					client.getNetworkHandler().sendChatCommand("shop");
				});
				stateMachine.setState(ScrapeStateMachine.State.SCANNING_MAIN);
			} else {
				// Done scanning
				finishScraping();
			}
		} else {
			stateMachine.incrementPage();
			stateMachine.setState(ScrapeStateMachine.State.NEXT_PAGE);
		}
	}

	private void handleNextPage(HandledScreen<?> screen) {
		stateMachine.setState(ScrapeStateMachine.State.SCANNING_CATEGORY);
	}

	private void clickItemByName(HandledScreen<?> screen, String itemName) {
		for (Slot slot : screen.getScreenHandler().slots) {
			ItemStack stack = slot.getStack();
			if (!stack.isEmpty() && stack.getName().getString().equals(itemName)) {
				clickSlot(screen, slot.getIndex());
				return;
			}
		}
	}

	private void clickSlot(HandledScreen<?> screen, int slotIndex) {
		ShopScraperMod.ACTION_QUEUE.enqueue(() -> {
			client.interactionManager.clickSlot(
				screen.getScreenHandler().syncId,
				slotIndex,
				0,
				net.minecraft.screen.slot.SlotActionType.PICKUP,
				client.player
			);
		});
	}

	private boolean isCategory(String itemName, ItemStack stack) {
		// Categories typically have category-like lore or are in certain positions
		// This is a heuristic; adjust based on actual shop format
		return itemName.contains("Category") || itemName.contains("Shop");
	}

	private boolean isNavigationItem(String itemName) {
		return itemName.contains(NEXT_PAGE_INDICATOR) || itemName.contains(BACK_INDICATOR)
			|| itemName.contains("Back") || itemName.contains("Close");
	}

	private HandledScreen<?> getCurrentScreen() {
		if (client.currentScreen instanceof HandledScreen) {
			return (HandledScreen<?>) client.currentScreen;
		}
		return null;
	}

	private void finishScraping() {
		CSVWriter.appendItems(stateMachine.getCollectedItems());
		ShopScraperMod.LOGGER.info("Scraping complete! {} items saved to CSV",
			stateMachine.getCollectedItems().size());
		stateMachine.reset();
		ShopScraperMod.ACTION_QUEUE.setActive(false);
	}
}
