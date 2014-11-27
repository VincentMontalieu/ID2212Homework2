package marketplace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import marketplace.exception.ItemExistsException;
import marketplace.exception.ItemNotOnSaleException;
import marketplace.exception.ItemTooExpensiveException;
import marketplace.exception.RejectedException;
import marketplace.exception.TraderExistsException;
import marketplace.exception.TraderNotExistsException;
import marketplace.exception.WishExistsException;
import bankrmi.Bank;
import client.Trader;

@SuppressWarnings("serial")
public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace {
	private List<Trader> registeredTraders;
	private List<Item> itemsOnSale;
	private List<Wish> wishes;
	private Bank bank;

	public MarketPlaceImpl(String name) throws RemoteException,
			MalformedURLException, NotBoundException {
		this.registeredTraders = new ArrayList<>();
		this.itemsOnSale = new ArrayList<>();
		this.wishes = new ArrayList<>();

		/* Get Remote Bank */
		this.bank = (Bank) Naming.lookup("Nordea");
	}

	@Override
	public synchronized void registerClient(Trader trader)
			throws RemoteException, TraderExistsException,
			TraderNotExistsException {
		if (this.registeredTraders.contains(trader))
			throw new TraderExistsException(trader);
		this.registeredTraders.add(trader);
	}

	@Override
	public synchronized void unRegisterClient(Trader trader)
			throws RemoteException, RejectedException {
		if (!this.registeredTraders.contains(trader))
			throw new TraderNotExistsException(trader);
		for (Item it : this.itemsOnSale) {
			if (it.getOwner().equals(trader)) {
				this.itemsOnSale.remove(it);
			}
		}
		for (Wish w : this.wishes) {
			if (w.getTrader().equals(trader)) {
				this.itemsOnSale.remove(w);
			}
		}
		this.registeredTraders.remove(trader);
	}

	@Override
	public synchronized void placeItemOnSale(Item item) throws RemoteException,
			RejectedException {
		if (this.itemsOnSale.contains(item))
			throw new ItemExistsException(item);
		this.itemsOnSale.add(item);
		this.inspectWishesForNotification(item);
	}

	@Override
	public synchronized void buyItem(Item item, Trader buyer)
			throws RemoteException, RejectedException,
			bankrmi.RejectedException {
		if (contains(item)) {
			throw new ItemNotOnSaleException(item);
		}

		Item actualItem = getItem(item);

		if (actualItem == null) {
			System.err.println("Purchase is not possible... System error.");
			return;
		}

		Float balance = this.bank.getAccount(buyer.getName()).getBalance();
		if (balance.compareTo(actualItem.getPrice()) <= 0) {
			throw new ItemTooExpensiveException(actualItem, balance);
		}

		Trader seller = actualItem.getOwner();

		this.bank.getAccount(seller.getName()).deposit(actualItem.getPrice());
		this.bank.getAccount(buyer.getName()).withdraw(actualItem.getPrice());

		this.itemsOnSale.remove(actualItem);
		seller.notifySale(actualItem);
	}

	private Item getItem(Item lookup) throws RemoteException {
		for (Item item : itemsOnSale) {
			if (item.getName().equalsIgnoreCase(lookup.getName())
					&& item.getPrice().equals(lookup.getPrice())
					&& item.getOwner().getName()
							.equalsIgnoreCase(lookup.getOwner().getName())) {
				return item;
			}
		}
		return null;
	}

	private boolean contains(Item lookup) throws RemoteException {
		for (Item item : itemsOnSale) {
			if (item.getName().equalsIgnoreCase(lookup.getName())
					&& item.getPrice() == lookup.getPrice()
					&& item.getOwner().getName()
							.equalsIgnoreCase(lookup.getOwner().getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized List<Item> getItemsOnSale() throws RemoteException {
		return this.itemsOnSale;
	}

	@Override
	public synchronized void placeWish(Wish wish) throws RemoteException,
			RejectedException {
		if (this.wishes.contains(wish))
			throw new WishExistsException(wish);
		this.wishes.add(wish);
		this.inspectItemsForNotification(wish);
	}

	private synchronized void inspectItemsForNotification(Wish wish)
			throws RemoteException {
		for (Item it : this.itemsOnSale) {
			if (it.getName().equalsIgnoreCase(wish.getItem().getName())) {
				if (it.getPrice().compareTo(wish.getItem().getPrice()) <= 0) {
					try {
						wish.getTrader().notifyWish(wish);
					} catch (RemoteException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private synchronized void inspectWishesForNotification(Item item)
			throws RemoteException {
		for (Wish w : this.wishes) {
			if (w.getItem().getName().equalsIgnoreCase(item.getName())) {
				if (w.getItem().getPrice().compareTo(item.getPrice()) >= 0) {
					try {
						w.getTrader().notifyWish(w);
					} catch (RemoteException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
}
