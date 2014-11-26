package marketplace;

import client.Trader;

public class ItemImpl implements Item {

	private String name;
	private Float price;
	private Trader owner;

	public ItemImpl(String name, Float price, Trader owner) {
		this.name = name;
		this.price = price;
		this.owner = owner;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Float getPrice() {
		return price;
	}

	@Override
	public Trader getOwner() {
		return owner;
	}

	@Override
	public void setOwner(Trader buyer) {
		this.owner = buyer;
	}
}
