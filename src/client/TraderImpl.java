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
	private SuperClient client;

	public TraderImpl(String login, SuperClient client) throws RemoteException,
			MalformedURLException {
		this.client = client;
		this.login = login;
		Naming.rebind("rmi://localhost/trader/" + this.login, this);
	}

	@Override
	public void notifySale(Item item) throws RemoteException {
		client.notifySale(item);
	}

	@Override
	public void notifyWish(Wish wish) throws RemoteException {
		client.notifyWish(wish);
	}

	@Override
	public String getName() {
		return this.login;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TraderImpl)) {
			return false;
		}
		TraderImpl other = (TraderImpl) obj;
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		return true;
	}
}
