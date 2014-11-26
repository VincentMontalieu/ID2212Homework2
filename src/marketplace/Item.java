package marketplace;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Trader;

public interface Item extends Remote {

	public String getName() throws RemoteException;

	public Float getPrice() throws RemoteException;

	public Trader getOwner() throws RemoteException;

	public void setOwner(Trader buyer) throws RemoteException;

}
