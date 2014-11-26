package marketplace.exception;

import client.Trader;

public class TraderExistsException extends RejectedException {
	protected Trader trader;

	public TraderExistsException(Trader trader) {
		this.trader = trader;
	}

	@Override
	public String toString() {
		return "To implement";
	}

}
