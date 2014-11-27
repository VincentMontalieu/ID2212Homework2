package marketplace.exception;

import marketplace.Item;

@SuppressWarnings("serial")
public class ItemExistsException extends RejectedException {
	protected Item item;

	public ItemExistsException(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Sorry, the item [ " + item + " ] already exists.";
	}

}
