package marketplace;

import java.io.Serializable;

import client.Trader;

public class WishImpl implements Wish, Serializable {

	private Item item;
	private Trader trader;

	public WishImpl(Item item, Trader trader) {
		this.item = item;
		this.trader = trader;
	}

	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public Trader getTrader() {
		return trader;
	}

}
