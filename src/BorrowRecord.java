package model;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 5L;

    private String recordId;
    private String memberId;
    private String itemId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;

    public BorrowRecord(String recordId, String memberId, String itemId) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14);
        this.returnDate = null;
        this.isReturned = false;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void markReturned() {
        this.returnDate = LocalDate.now();
        this.isReturned = true;
    }

    public boolean isOverdue() {
        if (isReturned) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        if (isReturned) {
            return dueDate.until(returnDate).getDays();
        }
        return dueDate.until(LocalDate.now()).getDays();
    }

    @Override
    public String toString() {
        return "[RECORD] ID: " + recordId +
               " | Member: " + memberId +
               " | Item: " + itemId +
               " | Borrowed: " + borrowDate +
               " | Due: " + dueDate +
               " | Returned: " + isReturned +
               " | Overdue: " + isOverdue();
    }
}
