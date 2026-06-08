package com.shopscraper.core;

import net.fabricmc.loader.api.FabricLoader;
import com.shopscraper.ShopScraperMod;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CSVWriter {
	private static final Path CSV_FILE = FabricLoader.getInstance()
		.getConfigDir()
		.resolve("shop_prices.csv");

	private static final String HEADER = "Item,Category,Buy Price,Sell Price";

	public static void initialize() {
		try {
			Files.createDirectories(CSV_FILE.getParent());

			// Create file with header if it doesn't exist
			if (!Files.exists(CSV_FILE)) {
				Files.writeString(CSV_FILE, HEADER + "\n", StandardCharsets.UTF_8);
				ShopScraperMod.LOGGER.info("Created new CSV file: {}", CSV_FILE);
			}
		} catch (IOException e) {
			ShopScraperMod.LOGGER.error("Failed to initialize CSV file", e);
		}
	}

	public static void appendItem(ScrapeStateMachine.ScrapedItem item) {
		try {
			String sanitizedName = sanitizeItemName(item.itemName);
			String sanitizedCategory = sanitizeItemName(item.category);
			String line = sanitizedName + "," + sanitizedCategory + "," +
				item.buyPrice + "," + item.sellPrice + "\n";

			Files.writeString(CSV_FILE, line, StandardCharsets.UTF_8,
				StandardOpenOption.APPEND, StandardOpenOption.CREATE);

			ShopScraperMod.LOGGER.debug("Appended item to CSV: {}", item.itemName);
		} catch (IOException e) {
			ShopScraperMod.LOGGER.error("Failed to append item to CSV", e);
		}
	}

	public static void appendItems(Iterable<ScrapeStateMachine.ScrapedItem> items) {
		try {
			StringBuilder sb = new StringBuilder();
			for (ScrapeStateMachine.ScrapedItem item : items) {
				String sanitizedName = sanitizeItemName(item.itemName);
				String sanitizedCategory = sanitizeItemName(item.category);
				sb.append(sanitizedName).append(",")
					.append(sanitizedCategory).append(",")
					.append(item.buyPrice).append(",")
					.append(item.sellPrice).append("\n");
			}

			if (sb.length() > 0) {
				Files.writeString(CSV_FILE, sb.toString(), StandardCharsets.UTF_8,
					StandardOpenOption.APPEND, StandardOpenOption.CREATE);
				ShopScraperMod.LOGGER.info("Appended {} items to CSV",
					sb.toString().split("\n").length);
			}
		} catch (IOException e) {
			ShopScraperMod.LOGGER.error("Failed to append items to CSV", e);
		}
	}

	/**
	 * Sanitizes item names for CSV format (removes commas, newlines, etc.)
	 */
	private static String sanitizeItemName(String name) {
		return name.replaceAll("[,\n\r\"']", " ").trim();
	}

	public static Path getCSVPath() {
		return CSV_FILE;
	}
}
