package com.shopscraper.modes;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.client.MinecraftClient;
import com.shopscraper.ShopScraperMod;
import com.shopscraper.core.LoreParser;
import com.shopscraper.core.ScrapeStateMachine;
import com.shopscraper.core.CSVWriter;

public class PacketScraper {
	private final ScrapeStateMachine stateMachine;
	private MinecraftClient client = MinecraftClient.getInstance();
	private int lastSyncId = -1;
	private boolean waitingForScreen = false;
	private static final String NEXT_PAGE_INDICATOR = "→";
	private static final String BACK_INDICATOR = "←";

	public PacketScraper(ScrapeStateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public void start() {
		stateMachine.setState(ScrapeStateMachine.State.OPENING_SHOP);
		ShopScraperMod.ACTION_QUEUE.setActive(true);
		waitingForScreen = true;

		// Queue command to open shop
		ShopScraperMod.ACTION_QUEUE.enqueue(() -> {
			client.getNetworkHandler().sendChatCommand("shop");
		});

		ShopScraperMod.LOGGER.info("Packet scraper started");
	}

	/**
	 * Called when the server sends an OpenScreen packet
	 */
	public void handleScreenOpen(int syncId, ItemStack[] items) {
		lastSyncId = syncId;

		if (waitingForScreen) {
			waitingForScreen = false;
			stateMachine.setState(ScrapeStateMachine.State.SCANNING_MAIN);
			scanItems(items);
		} else if (stateMachine.getCurrentState() == ScrapeStateMachine.State.OPENING_CATEGORY) {
			stateMachine.setState(ScrapeStateMachine.State.SCANNING_CATEGORY);
			scanItems(items);
		}
	}

	/**
	 * Called when inventory slots are updated
	 */
	public void handleSlotUpdate(int syncId, int slot, ItemStack item) {
		// Can be used to detect page changes or item updates
		if (syncId == lastSyncId && stateMachine.getCurrentState() == ScrapeStateMachine.State.SCANNING_CATEGORY) {
			// Parse individual item updates if needed
		}
	}

	private void scanItems(ItemStack[] items) {
		ScrapeStateMachine.State state = stateMachine.getCurrentState();

		if (state == ScrapeStateMachine.State.SCANNING_MAIN) {
			scanMainShop(items);
		} else if (state == ScrapeStateMachine.State.SCANNING_CATEGORY) {
			scanCategory(items);
		}
	}

	private void scanMainShop(ItemStack[] items) {
		int categoriesFound = 0;

		for (ItemStack stack : items) {
			if (stack.isEmpty()) continue;

			String itemName = stack.getName().getString();

			// Detect categories (heuristic-based)
			if (isCategory(itemName, stack)) {
				stateMachine.pushCategory(itemName);
				categoriesFound++;
			}
		}

		ShopScraperMod.LOGGER.info("Packet mode: Found {} categories", categoriesFound);

		if (categoriesFound > 0) {
			String category = stateMachine.popCategory();
			clickItemByName(items, category);
			stateMachine.setState(ScrapeStateMachine.State.OPENING_CATEGORY);
		} else {
			finishScraping();
		}
	}

	private void scanCategory(ItemStack[] items) {
		for (ItemStack stack : items) {
			if (stack.isEmpty()) continue;

			String itemName = stack.getName().getString();

			// Skip navigation items
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

		// Check for next page
		boolean hasNextPage = false;
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = items[i];
			if (!stack.isEmpty() && stack.getName().getString().contains(NEXT_PAGE_INDICATOR)) {
				hasNextPage = true;
				clickSlot(i);
				break;
			}
		}

		if (!hasNextPage) {
			// Try next category
			stateMachine.resetPage();
			String nextCategory = stateMachine.popCategory();
			if (nextCategory != null) {
				clickItemByName(items, nextCategory);
				stateMachine.setState(ScrapeStateMachine.State.OPENING_CATEGORY);
			} else {
				finishScraping();
			}
		} else {
			stateMachine.incrementPage();
		}
	}

	private void clickItemByName(ItemStack[] items, String itemName) {
		for (int i = 0; i < items.length; i++) {
			ItemStack stack = items[i];
			if (!stack.isEmpty() && stack.getName().getString().equals(itemName)) {
				clickSlot(i);
				return;
			}
		}
	}

	private void clickSlot(int slotIndex) {
		ShopScraperMod.ACTION_QUEUE.enqueue(() -> {
			ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
				lastSyncId,
				0, // Revision
				slotIndex,
				0, // Button
				SlotActionType.PICKUP,
				net.minecraft.util.collection.DefaultedList.create(),
				null
			);
			client.getNetworkHandler().sendPacket(packet);
		});
	}

	private boolean isCategory(String itemName, ItemStack stack) {
		return itemName.contains("Category") || itemName.contains("Shop");
	}

	private boolean isNavigationItem(String itemName) {
		return itemName.contains(NEXT_PAGE_INDICATOR) || itemName.contains(BACK_INDICATOR)
			|| itemName.contains("Back") || itemName.contains("Close");
	}

	private void finishScraping() {
		CSVWriter.appendItems(stateMachine.getCollectedItems());
		ShopScraperMod.LOGGER.info("Packet scraper complete! {} items saved to CSV",
			stateMachine.getCollectedItems().size());
		stateMachine.reset();
		ShopScraperMod.ACTION_QUEUE.setActive(false);
	}
}
