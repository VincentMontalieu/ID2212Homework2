/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Alex
 */
public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace {
    private String name;
    private List<Trader> registeredTraders;
    private List<Item> itemsOnSale;
    private List<Wish> wishes;
    private Bank bank;
    
    public MarketPlaceImpl(String name) throws RemoteException, MalformedURLException,
            NotBoundException {
        this.name = name;
        this.registeredTraders = new ArrayList<>();
        this.itemsOnSale = new ArrayList<>();
        this.wishes = new ArrayList<>();
        
        /* Get Remote Bank*/
        this.bank = (Bank) Naming.lookup("Nordea");
    }

    @Override
    public void registerClient(Trader trader) throws RemoteException, TraderExistsException, TraderNotExistsException {
        if ( this.registeredTraders.contains(trader) )
            throw new TraderExistsException(trader);
        this.registeredTraders.add(trader);
    }

    @Override
    public void unRegisterClient(Trader trader) throws RemoteException, RejectedException {
        if ( !this.registeredTraders.contains(trader) )
            throw new TraderNotExistsException(trader);
        /* Remove his items on sale */
        /* TODO : check that it is allowed to remove while performing a loop */
        for ( Item it : this.itemsOnSale ) {
            if ( it.getOwner().equals(trader) ) {
                this.itemsOnSale.remove(it);
            }
        }
        /* Remove his wishes */
        /* TODO : check that it is allowed to remove while performing a loop */
        for ( Wish w : this.wishes ) {
            if ( w.getTrader().equals(trader) ) {
                this.itemsOnSale.remove(w);
            }
        }
        this.registeredTraders.remove(trader);
    }

    @Override
    public void placeItemOnSale(Item item) throws RemoteException, RejectedException {
        if ( this.itemsOnSale.contains(item) )
            throw new ItemExistsException(item);
        this.itemsOnSale.add(item);
        this.inspectWishesForNotification(item);
    }

    @Override
    public void buyItem(Item item, Trader buyer) throws RemoteException, RejectedException {
        /* Item not (or no lnger) on sale */
        if ( !this.itemsOnSale.contains(item) )
            throw new ItemNotOnSaleException(item);
        
        /* Check account balance of the buyer */
        Float balance =
                this.bank.getAccount(item.getOwner().getName() ).getBalance();
        if ( balance.compareTo(item.getPrice() ) <= 0 ) {
            /* Not enough cash */
            throw new ItemTooExpensiveException(item, balance);
        }
        
        Trader seller = item.getOwner();
        
        /* The buyer can purchase the item, process bank operations */
        this.bank.getAccount(seller.getName() ).deposit(
                item.getPrice() );
        this.bank.getAccount(buyer.getName() ).withdraw(item.getPrice() );
        
        /* Change ownership */
        item.setOwner(buyer);
        
        /* Notify the seller */
        seller.notifySale(item);
    }

    @Override
    public List<Item> getItemsOnSale() throws RemoteException {
        return this.itemsOnSale;
    }

    @Override
    public void placeWish(Wish wish) throws RemoteException, RejectedException {
        if ( this.wishes.contains(wish) )
            throw new WishExistsException(wish);
        this.wishes.add(wish);
        this.inspectItemsForNotification(wish);
    }

    private void inspectItemsForNotification(Wish wish) {
        for ( Item it : this.itemsOnSale ) {
            if ( it.getName().equalsIgnoreCase(wish.getItemName() ) ) {
                if ( it.getPrice().compareTo(wish.getItemPrice() ) <= 0 ) {
                    try {
                        wish.getTrader().notifyWish(wish, it);
                    } catch (RemoteException ex) {
                        /* Trader no longer online, we could send an email */
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void inspectWishesForNotification(Item item) {
        for ( Wish w : this.wishes ) {
            if ( w.getItemName().equalsIgnoreCase(item.getName() ) ) {
                if ( w.getItemPrice().compareTo(item.getPrice() ) >= 0 ) {
                    try {
                        w.getTrader().notifyWish(w, item);
                    } catch (RemoteException ex) {
                        /* Trader no longer online, we could send an email */
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
}
