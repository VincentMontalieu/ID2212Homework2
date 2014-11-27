package marketplace.exception;

import java.rmi.RemoteException;

import client.Trader;

@SuppressWarnings("serial")
public class TraderNotExistsException extends RejectedException {
	protected Trader trader;

	public TraderNotExistsException(Trader trader) {
		this.trader = trader;
	}

	@Override
	public String toString() {
		try {
			return "Trader: " + trader.getName() + " doesn't exists.";
		} catch (RemoteException e) {
			e.printStackTrace();
			return "Error with traders data...";
		}
	}

}
