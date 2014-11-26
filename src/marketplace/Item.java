package marketplace;

import java.rmi.Remote;

import client.Trader;

public interface Item extends Remote {

	public String getName();

	public Float getPrice();

	public Trader getOwner();

	public void setOwner(Trader buyer);

}
