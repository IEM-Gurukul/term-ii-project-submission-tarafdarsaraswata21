package factory;

import model.Book;
import model.LibraryItem;
import model.Magazine;

public class LibraryItemFactory {

    private LibraryItemFactory() {
    }

    public static LibraryItem createItem(String type, String itemId, String title,
                                         String author, String extra1, String extra2) {

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Item type cannot be null or empty.");
        }

        switch (type.toUpperCase()) {

            case "BOOK":
                return new Book(itemId, title, author, extra1, extra2);

            case "MAGAZINE":
                int issueNumber;
                try {
                    issueNumber = Integer.parseInt(extra1);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Issue number must be a valid integer. Got: " + extra1);
                }
                return new Magazine(itemId, title, author, issueNumber, extra2);

            default:
                throw new IllegalArgumentException("Unknown item type: " + type +
                        ". Supported types are: BOOK, MAGAZINE");
        }
    }
}
