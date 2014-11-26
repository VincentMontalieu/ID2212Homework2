package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import marketplace.exception.RejectedException;
import client.Trader;

public interface MarketPlace extends Remote {
	public void registerClient(Trader trader) throws RemoteException,
			RejectedException;

	public void unRegisterClient(Trader trader) throws RemoteException,
			RejectedException;

	public void placeItemOnSale(Item item) throws RemoteException,
			RejectedException;

	public void buyItem(Item item, Trader buyer) throws RemoteException,
			RejectedException, bankrmi.RejectedException;

	public String[] getItemsOnSale() throws RemoteException;

	public void placeWish(Wish wish) throws RemoteException, RejectedException;

	public Trader getTrader(String name) throws RemoteException;
}
