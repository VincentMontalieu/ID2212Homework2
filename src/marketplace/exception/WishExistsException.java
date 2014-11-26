package marketplace.exception;

import marketplace.Wish;

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
