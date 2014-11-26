/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace;

import java.rmi.Remote;

import client.Trader;

/**
 *
 * @author Alex
 */
public interface Wish extends Remote {

    public String getItemName();

    public Float getItemPrice();

    public Trader getTrader();
    
}
