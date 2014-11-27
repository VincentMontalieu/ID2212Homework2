package marketplace.exception;

import marketplace.Wish;

@SuppressWarnings("serial")
public class WishExistsException extends RejectedException {
	protected Wish wish;

	public WishExistsException(Wish wish) {
		this.wish = wish;
	}

	@Override
	public String toString() {
		return "Sorry, the wish [ " + wish + " ] already exists.";
	}
}
