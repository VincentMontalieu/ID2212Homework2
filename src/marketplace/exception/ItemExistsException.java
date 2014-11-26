package marketplace.exception;

import marketplace.Item;

public class ItemExistsException extends RejectedException {
	protected Item item;

	public ItemExistsException(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "To implement";
	}

}
