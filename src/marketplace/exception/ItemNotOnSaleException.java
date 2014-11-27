package marketplace.exception;

import marketplace.Item;

@SuppressWarnings("serial")
public class ItemNotOnSaleException extends RejectedException {
	protected Item item;

	public ItemNotOnSaleException(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Sorry, the item [ " + item + " ] isn't available for sale.";
	}
}
