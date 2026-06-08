package com.shopscraper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shopscraper.command.ScrapeCommand;
import com.shopscraper.config.ScraperConfig;
import com.shopscraper.core.ActionQueue;

public class ShopScraperMod implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("shop-scraper");
	public static final ActionQueue ACTION_QUEUE = new ActionQueue();
	public static final ScraperConfig CONFIG = new ScraperConfig();

	@Override
	public void onInitializeClient() {
		LOGGER.info("Shop Scraper mod initialized!");

		// Load config
		CONFIG.load();

		// Register action queue tick
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ACTION_QUEUE.tick();
		});

		// Register commands
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ScrapeCommand.register(dispatcher);
		});

		LOGGER.info("Shop Scraper ready!");
	}
}
