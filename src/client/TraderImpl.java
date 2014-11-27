package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marketplace.Item;
import marketplace.Wish;

@SuppressWarnings("serial")
public class TraderImpl extends UnicastRemoteObject implements Trader {
	private String login;

	public TraderImpl(String login) throws RemoteException,
			MalformedURLException {
		this.login = login;
		Naming.rebind("rmi://localhost/trader/" + this.login, this);
	}

	@Override
	public void notifySale(Item item) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void notifyWish(Wish wish, Item item) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getName() {
		return this.login;
	}
}
