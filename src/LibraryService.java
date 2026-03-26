package service;

import exception.AlreadyBorrowedException;
import exception.BookNotFoundException;
import exception.MemberNotFoundException;
import factory.LibraryItemFactory;
import model.BorrowRecord;
import model.LibraryItem;
import model.Member;
import strategy.FineStrategy;
import strategy.RegularFineStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {

    private static LibraryService instance;

    private Map<String, LibraryItem> items;
    private Map<String, Member> members;
    private Map<String, BorrowRecord> borrowRecords;
    private FineStrategy fineStrategy;

    private LibraryService() {
        this.items = new HashMap<>();
        this.members = new HashMap<>();
        this.borrowRecords = new HashMap<>();
        this.fineStrategy = new RegularFineStrategy();
    }

    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    public void setFineStrategy(FineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }

    public FineStrategy getFineStrategy() {
        return fineStrategy;
    }

    // ── ITEM MANAGEMENT ──

    public void addItem(String type, String itemId, String title,
                        String author, String extra1, String extra2) {
        LibraryItem item = LibraryItemFactory.createItem(type, itemId, title, author, extra1, extra2);
        items.put(itemId, item);
        System.out.println("Added: " + item.getDetails());
    }

    public void removeItem(String itemId) throws BookNotFoundException {
        if (!items.containsKey(itemId)) {
            throw new BookNotFoundException(itemId);
        }
        LibraryItem removed = items.remove(itemId);
        System.out.println("Removed: " + removed.getTitle());
    }

    public LibraryItem searchItemById(String itemId) throws BookNotFoundException {
        LibraryItem item = items.get(itemId);
        if (item == null) {
            throw new BookNotFoundException(itemId);
        }
        return item;
    }

    public List<LibraryItem> searchItemsByTitle(String keyword) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<LibraryItem> searchItemsByAuthor(String author) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    // ── MEMBER MANAGEMENT ──

    public void addMember(String memberId, String name, String email) {
        Member member = new Member(memberId, name, email);
        members.put(memberId, member);
        System.out.println("Registered member: " + member.getName());
    }

    public void removeMember(String memberId) throws MemberNotFoundException {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        Member removed = members.remove(memberId);
        System.out.println("Removed member: " + removed.getName());
    }

    public Member getMember(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }
        return member;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    public Map<String, LibraryItem> getItems() {
        return items;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    // ── BORROW AND RETURN ──

    public BorrowRecord issueItem(String memberId, String itemId)
            throws MemberNotFoundException, BookNotFoundException, AlreadyBorrowedException {

        Member member = getMember(memberId);
        LibraryItem item = searchItemById(itemId);

        if (!item.isAvailable()) {
            throw new AlreadyBorrowedException(itemId);
        }

        String recordId = "REC" + System.currentTimeMillis();
        BorrowRecord record = new BorrowRecord(recordId, memberId, itemId);

        item.setAvailable(false);
        member.addBorrowRecord(record);
        borrowRecords.put(recordId, record);

        System.out.println("Issued '" + item.getTitle() + "' to " + member.getName());
        System.out.println("Due date: " + record.getDueDate());

        return record;
    }

    public double returnItem(String memberId, String itemId)
            throws MemberNotFoundException, BookNotFoundException {

        Member member = getMember(memberId);
        LibraryItem item = searchItemById(itemId);

        BorrowRecord record = null;
        for (BorrowRecord r : member.getBorrowHistory()) {
            if (r.getItemId().equals(itemId) && !r.isReturned()) {
                record = r;
                break;
            }
        }

        if (record == null) {
            System.out.println("No active borrow record found for this item and member.");
            return 0.0;
        }

        record.markReturned();
        item.setAvailable(true);

        double fine = fineStrategy.calculateFine(record.getDaysOverdue());

        System.out.println("Returned '" + item.getTitle() + "' by " + member.getName());
        if (fine > 0) {
            System.out.println("Overdue fine (" + fineStrategy.getStrategyName() + "): Rs. " + fine);
        } else {
            System.out.println("Returned on time. No fine.");
        }

        return fine;
    }

    public void printBorrowHistory(String memberId) throws MemberNotFoundException {
        Member member = getMember(memberId);
        List<BorrowRecord> history = member.getBorrowHistory();

        if (history.isEmpty()) {
            System.out.println(member.getName() + " has no borrow history.");
            return;
        }

        System.out.println("Borrow history for " + member.getName() + ":");
        for (BorrowRecord record : history) {
            System.out.println("  " + record);
        }
    }
}
