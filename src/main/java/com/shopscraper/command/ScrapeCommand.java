package com.shopscraper.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import com.shopscraper.ShopScraperMod;
import com.shopscraper.core.CSVWriter;
import com.shopscraper.core.ScrapeStateMachine;
import com.shopscraper.modes.VisualScraper;
import com.shopscraper.modes.PacketScraper;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class ScrapeCommand {
	private static final ScrapeStateMachine STATE_MACHINE = new ScrapeStateMachine();
	private static VisualScraper visualScraper;
	private static PacketScraper packetScraper;
	private static boolean isRunning = false;

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		// Initialize scrapers
		visualScraper = new VisualScraper(STATE_MACHINE);
		packetScraper = new PacketScraper(STATE_MACHINE);

		// Initialize CSV
		CSVWriter.initialize();

		dispatcher.register(literal("scrape")
			.then(literal("visual")
				.executes(ctx -> {
					if (isRunning) {
						ctx.getSource().sendFeedback(Text.literal("§cScraping already in progress!"));
						return 1;
					}
					isRunning = true;
					STATE_MACHINE.reset();
					visualScraper.start();
					ctx.getSource().sendFeedback(Text.literal("§aStarted visual mode scraping..."));
					return 1;
				}))
			.then(literal("packet")
				.executes(ctx -> {
					if (isRunning) {
						ctx.getSource().sendFeedback(Text.literal("§cScraping already in progress!"));
						return 1;
					}
					isRunning = true;
					STATE_MACHINE.reset();
					packetScraper.start();
					ctx.getSource().sendFeedback(Text.literal("§aStarted packet mode scraping..."));
					return 1;
				}))
			.then(literal("stop")
				.executes(ctx -> {
					if (!isRunning) {
						ctx.getSource().sendFeedback(Text.literal("§cNo scraping in progress!"));
						return 1;
					}
					isRunning = false;
					ShopScraperMod.ACTION_QUEUE.clear();
					STATE_MACHINE.reset();
					ctx.getSource().sendFeedback(Text.literal("§aScraping stopped!"));
					return 1;
				}))
			.then(literal("status")
				.executes(ctx -> {
					String status = isRunning ? "§aRunning" : "§cIdle";
					ctx.getSource().sendFeedback(Text.literal("§6Shop Scraper Status: " + status));
					ctx.getSource().sendFeedback(Text.literal("§6Queue Size: " + ShopScraperMod.ACTION_QUEUE.getQueueSize()));
					ctx.getSource().sendFeedback(Text.literal("§6CSV: " + CSVWriter.getCSVPath()));
					return 1;
				}))
			.then(literal("config")
				.then(literal("delay")
					.then(argument("milliseconds", string())
						.executes(ctx -> {
							try {
								int delay = Integer.parseInt(ctx.getArgument("milliseconds", String.class));
								ShopScraperMod.CONFIG.setActionDelayMs(delay);
								ctx.getSource().sendFeedback(Text.literal("§aSet delay to " + delay + "ms"));
								return 1;
							} catch (NumberFormatException e) {
								ctx.getSource().sendError(Text.literal("§cInvalid number!"));
								return 0;
							}
						})))
				.then(literal("debug")
					.executes(ctx -> {
						boolean newDebugMode = !ShopScraperMod.CONFIG.isDebugMode();
						ShopScraperMod.CONFIG.setDebugMode(newDebugMode);
						ctx.getSource().sendFeedback(Text.literal("§aDebug mode: " + (newDebugMode ? "§aON" : "§cOFF")));
						return 1;
					})))
		);
	}

	public static VisualScraper getVisualScraper() {
		return visualScraper;
	}

	public static PacketScraper getPacketScraper() {
		return packetScraper;
	}

	public static ScrapeStateMachine getStateMachine() {
		return STATE_MACHINE;
	}

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean running) {
		isRunning = running;
	}
}
