package model;

public class Book extends LibraryItem {

    private static final long serialVersionUID = 2L;

    private String isbn;
    private String genre;

    public Book(String itemId, String title, String author, String isbn, String genre) {
        super(itemId, title, author);
        this.isbn = isbn;
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String getDetails() {
        return "[BOOK] ID: " + getItemId() +
               " | Title: " + getTitle() +
               " | Author: " + getAuthor() +
               " | ISBN: " + isbn +
               " | Genre: " + genre +
               " | Available: " + isAvailable();
    }
}
