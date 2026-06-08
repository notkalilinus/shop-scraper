package com.shopscraper.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class ScrapeStateMachine {
	public enum State {
		IDLE,
		OPENING_SHOP,
		SCANNING_MAIN,
		OPENING_CATEGORY,
		SCANNING_CATEGORY,
		NEXT_PAGE,
		RETURNING_MAIN
	}

	private State currentState = State.IDLE;
	private final Deque<String> categoryStack = new ArrayDeque<>();
	private final Set<String> visitedCategories = new HashSet<>();
	private String currentCategory = null;
	private int currentPage = 1;
	private final Deque<ScrapedItem> collectedItems = new ArrayDeque<>();

	public State getCurrentState() {
		return currentState;
	}

	public void setState(State newState) {
		currentState = newState;
	}

	public void pushCategory(String categoryName) {
		if (!visitedCategories.contains(categoryName)) {
			categoryStack.push(categoryName);
		}
	}

	public String popCategory() {
		currentCategory = categoryStack.poll();
		if (currentCategory != null) {
			visitedCategories.add(currentCategory);
		}
		return currentCategory;
	}

	public String getCurrentCategory() {
		return currentCategory;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int page) {
		this.currentPage = page;
	}

	public void incrementPage() {
		currentPage++;
	}

	public void resetPage() {
		currentPage = 1;
	}

	public void addItem(ScrapedItem item) {
		collectedItems.add(item);
	}

	public Deque<ScrapedItem> getCollectedItems() {
		return collectedItems;
	}

	public void reset() {
		currentState = State.IDLE;
		categoryStack.clear();
		visitedCategories.clear();
		currentCategory = null;
		currentPage = 1;
		collectedItems.clear();
	}

	public static class ScrapedItem {
		public String itemName;
		public String category;
		public String buyPrice;
		public String sellPrice;

		public ScrapedItem(String itemName, String category, String buyPrice, String sellPrice) {
			this.itemName = itemName;
			this.category = category;
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
		}

		@Override
		public String toString() {
			return itemName + "," + category + "," + buyPrice + "," + sellPrice;
		}
	}
}
