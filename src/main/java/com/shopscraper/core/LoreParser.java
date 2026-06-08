package com.shopscraper.core;

import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoreParser {
	// Regex patterns for price extraction
	private static final Pattern BUY_PATTERN = Pattern.compile(
		"(?i)(?:Buy|Cost|Price)\\s*[:\\-]?\\s*[$]?([\\d,]+(?:\\.\\d+)?[kKmM]?)",
		Pattern.CASE_INSENSITIVE
	);

	private static final Pattern SELL_PATTERN = Pattern.compile(
		"(?i)(?:Sell|Value|Worth)\\s*[:\\-]?\\s*[$]?([\\d,]+(?:\\.\\d+)?[kKmM]?)",
		Pattern.CASE_INSENSITIVE
	);

	/**
	 * Extracts lore text from an item stack (1.21.1 uses DataComponentTypes)
	 */
	public static List<String> extractLore(ItemStack stack) {
		List<String> loreLines = new ArrayList<>();

		var loreComponent = stack.get(DataComponentTypes.LORE);
		if (loreComponent != null) {
			for (Text line : loreComponent.lines()) {
				String plainText = line.getString();
				// Remove color codes and formatting
				plainText = plainText.replaceAll("§[0-9a-fk-or]", "");
				loreLines.add(plainText.trim());
			}
		}

		return loreLines;
	}

	/**
	 * Extracts buy price from lore lines
	 */
	public static String extractBuyPrice(List<String> loreLines) {
		for (String line : loreLines) {
			Matcher matcher = BUY_PATTERN.matcher(line);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "";
	}

	/**
	 * Extracts sell price from lore lines
	 */
	public static String extractSellPrice(List<String> loreLines) {
		for (String line : loreLines) {
			Matcher matcher = SELL_PATTERN.matcher(line);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "";
	}

	/**
	 * Extracts both prices from an item
	 */
	public static PriceData extractPrices(ItemStack stack) {
		List<String> lore = extractLore(stack);
		String buyPrice = extractBuyPrice(lore);
		String sellPrice = extractSellPrice(lore);
		return new PriceData(buyPrice, sellPrice);
	}

	public static class PriceData {
		public String buyPrice;
		public String sellPrice;

		public PriceData(String buyPrice, String sellPrice) {
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
		}

		@Override
		public String toString() {
			return "Buy: " + buyPrice + ", Sell: " + sellPrice;
		}
	}
}
