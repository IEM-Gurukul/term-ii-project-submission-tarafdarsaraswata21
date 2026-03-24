package exception;

public class AlreadyBorrowedException extends Exception {

    private static final long serialVersionUID = 7L;

    public AlreadyBorrowedException(String itemId) {
        super("Item with ID: " + itemId + " is already borrowed and not available.");
    }
}
