package marketplace;

import java.rmi.Remote;

import client.Trader;

public interface Wish extends Remote {

	public Item getItem();

	public Float getItemPrice();

	public Trader getTrader();

}
