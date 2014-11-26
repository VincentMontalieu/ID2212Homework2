package marketplace.exception;

import marketplace.Item;

public class ItemTooExpensiveException extends RejectedException {
	protected Item item;
	protected Float currentBalance;

	public ItemTooExpensiveException(Item item, Float balance) {
		this.item = item;
		this.currentBalance = balance;
	}

	@Override
	public String toString() {
		return "To implement";
	}
}
