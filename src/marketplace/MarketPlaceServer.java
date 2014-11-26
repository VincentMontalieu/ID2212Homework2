package marketplace;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MarketPlaceServer {
	private static final String USAGE = "java marketplace.MarketPlaceServer "
			+ "<market_rmi_url>";
	private static final String MARKET = "Agora";

	public MarketPlaceServer(String marketName) {
		try {
			MarketPlace market = new MarketPlaceImpl(marketName);
			// Register the newly created object at rmiregistry.
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException e) {
				LocateRegistry.createRegistry(1099);
			}
			Naming.rebind(marketName, market);
			System.out.println(market + " is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length > 1
				|| (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}

		String marketName = null;
		if (args.length > 0) {
			marketName = args[0];
		} else {
			marketName = MARKET;
		}

		new MarketPlaceServer(marketName);
	}
}
