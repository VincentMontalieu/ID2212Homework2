/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace.exception;

import marketplace.Item;

/**
 *
 * @author Alex
 */
public class ItemTooExpensiveException extends RejectedException {
    protected Item item;
    protected Float currentBalance;
    
    public ItemTooExpensiveException(Item item, Float balance) {
        this.item = item;
        this.currentBalance = balance;
    }

    @Override
    public String toString() {
        return "To implement";
    }
}
