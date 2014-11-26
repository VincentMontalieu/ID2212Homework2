package marketplace.exception;

import client.Trader;

public class TraderNotExistsException extends RejectedException {
	protected Trader trader;

	public TraderNotExistsException(Trader trader) {
		this.trader = trader;
	}

	@Override
	public String toString() {
		return "To implement";
	}

}
