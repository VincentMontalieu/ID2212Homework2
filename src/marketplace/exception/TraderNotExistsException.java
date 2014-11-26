/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace.exception;

import client.Trader;

/**
 *
 * @author Alex
 */
public class TraderNotExistsException extends RejectedException {
    protected Trader trader;
    
    public TraderNotExistsException(Trader trader) {
        this.trader = trader;
    }

    @Override
    public String toString() {
        return "To implement";
    }
    
    
    
}
