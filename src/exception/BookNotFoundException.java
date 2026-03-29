package exception;

public class BookNotFoundException extends Exception {

    private static final long serialVersionUID = 6L;

    public BookNotFoundException(String itemId) {
        super("No library item found with ID: " + itemId);
    }
}
