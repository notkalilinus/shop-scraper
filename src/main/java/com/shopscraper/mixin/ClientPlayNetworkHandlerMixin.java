package com.shopscraper.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.item.ItemStack;
import com.shopscraper.command.ScrapeCommand;
import com.shopscraper.ShopScraperMod;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Inject(method = "onOpenScreen", at = @At("TAIL"))
	private void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
		if (!ScrapeCommand.isRunning()) {
			return;
		}

		int syncId = packet.getSyncId();
		ShopScraperMod.LOGGER.debug("Screen opened with syncId: {}", syncId);

		// The packet scraper will handle this
		// For now, we just log it
	}

	@Inject(method = "onInventory", at = @At("TAIL"))
	private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
		if (!ScrapeCommand.isRunning()) {
			return;
		}

		ShopScraperMod.LOGGER.debug("Inventory packet received");

		// Extract items from the inventory packet
		// This would require accessing the packet's items field
		// In Fabric 1.21.1, this might need reflection or access wideners
	}

	@Inject(method = "onScreenHandlerSlotUpdate", at = @At("TAIL"))
	private void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
		if (!ScrapeCommand.isRunning()) {
			return;
		}

		ShopScraperMod.LOGGER.debug("Slot update - syncId: {}, slot: {}",
			packet.getSyncId(), packet.getSlot());
	}
}
