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
public interface Item extends Remote {

    public String getName();

    public Float getPrice();

    public Trader getOwner();

    public void setOwner(Trader buyer);
    
}
