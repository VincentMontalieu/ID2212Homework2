/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketplace.exception;

import marketplace.Wish;

/**
 *
 * @author Alex
 */
public class WishExistsException extends RejectedException {
    protected Wish wish;
    
    public WishExistsException(Wish wish) {
        this.wish = wish;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}