/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 
 * @author Guillaume
 */
public class MarketPlaceServer {
	private static final String USAGE = "java marketplace.MarketPlaceServer "
			+ "<market_rmi_url>";
	private static final String MARKET = "Agora";

	public MarketPlaceServer(String MarketName) {
		try {
			MarketPlace market = new MarketPlaceImpl(MarketName);

			/* Register the marketplace to the RMI naming service */
			Naming.rebind(MarketName, market);
			System.out.println(market + " is ready.");
		} catch (RemoteException | MalformedURLException | NotBoundException ex) {
			ex.printStackTrace();
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
