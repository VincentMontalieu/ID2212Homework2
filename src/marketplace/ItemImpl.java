package marketplace;

import java.io.Serializable;
import java.rmi.RemoteException;

import client.Trader;

@SuppressWarnings("serial")
public class ItemImpl implements Item, Serializable {

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

	public String toString() {
		try {
			return name + " $" + price + " " + owner.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			return "Error with items data...";
		}
	}
}
