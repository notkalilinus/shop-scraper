package com.shopscraper.config;

import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ScraperConfig {
	private static final Path CONFIG_FILE = FabricLoader.getInstance()
		.getConfigDir()
		.resolve("shop-scraper.properties");

	private int actionDelayMs = 250; // Default 250ms delay between actions
	private boolean debugMode = false;

	public void load() {
		try {
			Files.createDirectories(CONFIG_FILE.getParent());

			Properties props = new Properties();
			if (Files.exists(CONFIG_FILE)) {
				props.load(Files.newInputStream(CONFIG_FILE));
				actionDelayMs = Integer.parseInt(props.getProperty("action_delay_ms", "250"));
				debugMode = Boolean.parseBoolean(props.getProperty("debug_mode", "false"));
			} else {
				save();
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config", e);
		}
	}

	public void save() {
		try {
			Files.createDirectories(CONFIG_FILE.getParent());
			Properties props = new Properties();
			props.setProperty("action_delay_ms", String.valueOf(actionDelayMs));
			props.setProperty("debug_mode", String.valueOf(debugMode));
			props.store(Files.newOutputStream(CONFIG_FILE), "Shop Scraper Configuration");
		} catch (IOException e) {
			throw new RuntimeException("Failed to save config", e);
		}
	}

	public int getActionDelayMs() {
		return actionDelayMs;
	}

	public void setActionDelayMs(int delayMs) {
		this.actionDelayMs = delayMs;
		save();
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debug) {
		this.debugMode = debug;
		save();
	}
}
