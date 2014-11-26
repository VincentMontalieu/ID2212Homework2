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
public class ItemExistsException extends RejectedException {
    protected Item item;
    
    public ItemExistsException(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "To implement";
    }
    
    
    
}
