package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

    private static final long serialVersionUID = 4L;

    private String memberId;
    private String name;
    private String email;
    private List<BorrowRecord> borrowHistory;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowHistory = new ArrayList<>();
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<BorrowRecord> getBorrowHistory() {
        return borrowHistory;
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowHistory.add(record);
    }

    @Override
    public String toString() {
        return "[MEMBER] ID: " + memberId +
               " | Name: " + name +
               " | Email: " + email +
               " | Books Borrowed: " + borrowHistory.size();
    }
}
