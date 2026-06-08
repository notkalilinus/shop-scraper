package com.shopscraper.core;

import java.util.LinkedList;
import java.util.Queue;
import com.shopscraper.ShopScraperMod;

public class ActionQueue {
	private final Queue<Runnable> actionQueue = new LinkedList<>();
	private long lastActionTime = 0;
	private boolean active = false;

	public void enqueue(Runnable action) {
		actionQueue.offer(action);
	}

	public void tick() {
		if (!active || actionQueue.isEmpty()) {
			return;
		}

		long currentTime = System.currentTimeMillis();
		long elapsed = currentTime - lastActionTime;

		if (elapsed >= ShopScraperMod.CONFIG.getActionDelayMs()) {
			Runnable action = actionQueue.poll();
			if (action != null) {
				try {
					action.run();
					lastActionTime = currentTime;
				} catch (Exception e) {
					ShopScraperMod.LOGGER.error("Error executing queued action", e);
				}
			}
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		if (!active) {
			actionQueue.clear();
		}
		lastActionTime = System.currentTimeMillis();
	}

	public boolean isActive() {
		return active;
	}

	public int getQueueSize() {
		return actionQueue.size();
	}

	public void clear() {
		actionQueue.clear();
		active = false;
	}
}
