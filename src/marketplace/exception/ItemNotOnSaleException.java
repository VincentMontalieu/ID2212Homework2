package marketplace.exception;

import marketplace.Item;

public class ItemNotOnSaleException extends RejectedException {
	protected Item item;

	public ItemNotOnSaleException(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "To implement";
	}
}
