package model;

public class Magazine extends LibraryItem {

    private static final long serialVersionUID = 3L;

    private int issueNumber;
    private String month;

    public Magazine(String itemId, String title, String author, int issueNumber, String month) {
        super(itemId, title, author);
        this.issueNumber = issueNumber;
        this.month = month;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public String getMonth() {
        return month;
    }

    @Override
    public String getDetails() {
        return "[MAGAZINE] ID: " + getItemId() +
               " | Title: " + getTitle() +
               " | Author: " + getAuthor() +
               " | Issue: " + issueNumber +
               " | Month: " + month +
               " | Available: " + isAvailable();
    }
}
