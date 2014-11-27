package marketplace.exception;

import java.rmi.RemoteException;

import client.Trader;

@SuppressWarnings("serial")
public class TraderExistsException extends RejectedException {
	protected Trader trader;

	public TraderExistsException(Trader trader) {
		this.trader = trader;
	}

	@Override
	public String toString() {
		try {
			return "Trader: " + trader.getName() + " already exists.";
		} catch (RemoteException e) {
			e.printStackTrace();
			return "Error with traders data...";
		}
	}
}
