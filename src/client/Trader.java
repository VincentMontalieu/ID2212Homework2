package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marketplace.Item;
import marketplace.Wish;

public interface Trader extends Remote {

	public void notifySale(Item item) throws RemoteException;

	public void notifyWish(Wish wish, Item item) throws RemoteException;

	public String getName();
}
