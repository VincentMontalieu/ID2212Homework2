package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Trader;

public interface Wish extends Remote {

	public Item getItem() throws RemoteException;

	public Trader getTrader() throws RemoteException;

}
