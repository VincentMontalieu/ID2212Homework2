/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marketplace.Item;
import marketplace.Wish;

/**
 *
 * @author Alex
 */
public interface Trader extends Remote {
	/* Notify the trader that its Item 'item' has been sold */
	public void notifySale(Item item) throws RemoteException;

	/* Notify the trader that the Item 'item' matches his Wish 'wish' */
	public void notifyWish(Wish wish, Item item) throws RemoteException;

	public String getName();
}
