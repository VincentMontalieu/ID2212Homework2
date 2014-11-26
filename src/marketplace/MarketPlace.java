/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import marketplace.exception.RejectedException;
import client.Trader;

/**
 *
 * @author Alex
 */
public interface MarketPlace extends Remote {
	public void registerClient(Trader trader) throws RemoteException,
			RejectedException;

	public void unRegisterClient(Trader trader) throws RemoteException,
			RejectedException;

	public void placeItemOnSale(Item item) throws RemoteException,
			RejectedException;

	public void buyItem(Item item, Trader buyer) throws RemoteException,
			RejectedException, bankrmi.RejectedException;

	public List<Item> getItemsOnSale() throws RemoteException;

	public void placeWish(Wish wish) throws RemoteException, RejectedException;
}
