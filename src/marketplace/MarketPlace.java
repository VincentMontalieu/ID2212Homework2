package marketplace;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

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
}
