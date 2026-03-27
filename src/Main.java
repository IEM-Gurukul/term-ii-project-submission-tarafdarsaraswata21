package ui;

import exception.AlreadyBorrowedException;
import exception.BookNotFoundException;
import exception.MemberNotFoundException;
import model.LibraryItem;
import model.Member;
import service.DataManager;
import service.LibraryService;
import strategy.PremiumFineStrategy;
import strategy.RegularFineStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final LibraryService service = LibraryService.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("   Welcome to Smart Library System");
        System.out.println("=========================================");

        DataManager.loadAll(service);

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();

            switch (choice) {
                case 1:  handleAddItem();         break;
                case 2:  handleRemoveItem();       break;
                case 3:  handleSearchItems();      break;
                case 4:  handleListAllItems();     break;
                case 5:  handleAddMember();        break;
                case 6:  handleRemoveMember();     break;
                case 7:  handleListAllMembers();   break;
                case 8:  handleIssueItem();        break;
                case 9:  handleReturnItem();       break;
                case 10: handleBorrowHistory();    break;
                case 11: handleChangeFineStrategy(); break;
                case 12:
                    DataManager.saveAll(service.getItems(), service.getMembers());
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ── MENU DISPLAY ──

    private static void printMainMenu() {
        System.out.println("\n========= MAIN MENU =========");
        System.out.println(" -- Library Items --");
        System.out.println("  1. Add Item (Book/Magazine)");
        System.out.println("  2. Remove Item");
        System.out.println("  3. Search Items");
        System.out.println("  4. List All Items");
        System.out.println(" -- Members --");
        System.out.println("  5. Add Member");
        System.out.println("  6. Remove Member");
        System.out.println("  7. List All Members");
        System.out.println(" -- Borrowing --");
        System.out.println("  8. Issue Item to Member");
        System.out.println("  9. Return Item");
        System.out.println(" 10. View Member Borrow History");
        System.out.println(" -- Settings --");
        System.out.println(" 11. Change Fine Strategy");
        System.out.println(" 12. Save and Exit");
        System.out.println("==============================");
        System.out.print("Enter choice: ");
    }

    // ── ITEM HANDLERS ──

    private static void handleAddItem() {
        System.out.print("Type (BOOK/MAGAZINE): ");
        String type = scanner.nextLine().trim();

        System.out.print("Item ID: ");
        String itemId = scanner.nextLine().trim();

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Author: ");
        String author = scanner.nextLine().trim();

        if (type.equalsIgnoreCase("BOOK")) {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Genre: ");
            String genre = scanner.nextLine().trim();
            try {
                service.addItem("BOOK", itemId, title, author, isbn, genre);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else if (type.equalsIgnoreCase("MAGAZINE")) {
            System.out.print("Issue Number: ");
            String issue = scanner.nextLine().trim();
            System.out.print("Month: ");
            String month = scanner.nextLine().trim();
            try {
                service.addItem("MAGAZINE", itemId, title, author, issue, month);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else {
            System.out.println("Unknown type. Use BOOK or MAGAZINE.");
        }
    }

    private static void handleRemoveItem() {
        System.out.print("Enter Item ID to remove: ");
        String itemId = scanner.nextLine().trim();
        try {
            service.removeItem(itemId);
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleSearchItems() {
        System.out.println("Search by: 1. Title  2. Author  3. ID");
        System.out.print("Choice: ");
        int choice = readInt();

        switch (choice) {
            case 1:
                System.out.print("Enter title keyword: ");
                String titleKey = scanner.nextLine().trim();
                List<LibraryItem> byTitle = service.searchItemsByTitle(titleKey);
                printItemList(byTitle);
                break;
            case 2:
                System.out.print("Enter author name: ");
                String authorKey = scanner.nextLine().trim();
                List<LibraryItem> byAuthor = service.searchItemsByAuthor(authorKey);
                printItemList(byAuthor);
                break;
            case 3:
                System.out.print("Enter Item ID: ");
                String id = scanner.nextLine().trim();
                try {
                    LibraryItem item = service.searchItemById(id);
                    System.out.println(item.getDetails());
                } catch (BookNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid search option.");
        }
    }

    private static void handleListAllItems() {
        List<LibraryItem> all = service.getAllItems();
        if (all.isEmpty()) {
            System.out.println("No items in the library.");
            return;
        }
        System.out.println("\n--- All Library Items ---");
        printItemList(all);
    }

    // ── MEMBER HANDLERS ──

    private static void handleAddMember() {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        service.addMember(memberId, name, email);
    }

    private static void handleRemoveMember() {
        System.out.print("Enter Member ID to remove: ");
        String memberId = scanner.nextLine().trim();
        try {
            service.removeMember(memberId);
        } catch (MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleListAllMembers() {
        List<Member> all = service.getAllMembers();
        if (all.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        System.out.println("\n--- All Members ---");
        for (Member m : all) {
            System.out.println(m);
        }
    }

    // ── BORROW HANDLERS ──

    private static void handleIssueItem() {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Item ID: ");
        String itemId = scanner.nextLine().trim();
        try {
            service.issueItem(memberId, itemId);
        } catch (MemberNotFoundException | BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (AlreadyBorrowedException e) {
            System.out.println("Cannot issue: " + e.getMessage());
        }
    }

    private static void handleReturnItem() {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Item ID: ");
        String itemId = scanner.nextLine().trim();
        try {
            service.returnItem(memberId, itemId);
        } catch (MemberNotFoundException | BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleBorrowHistory() {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        try {
            service.printBorrowHistory(memberId);
        } catch (MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── SETTINGS HANDLERS ──

    private static void handleChangeFineStrategy() {
        System.out.println("Select Fine Strategy:");
        System.out.println("  1. Regular (Rs. 2.00/day, no cap)");
        System.out.println("  2. Premium (Rs. 1.00/day, max Rs. 30)");
        System.out.print("Choice: ");
        int choice = readInt();
        if (choice == 1) {
            service.setFineStrategy(new RegularFineStrategy());
            System.out.println("Fine strategy set to: " + service.getFineStrategy().getStrategyName());
        } else if (choice == 2) {
            service.setFineStrategy(new PremiumFineStrategy());
            System.out.println("Fine strategy set to: " + service.getFineStrategy().getStrategyName());
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // ── HELPERS ──

    private static void printItemList(List<LibraryItem> items) {
        if (items.isEmpty()) {
            System.out.println("No items found.");
            return;
        }
        for (LibraryItem item : items) {
            System.out.println(item.getDetails());
        }
    }

    private static int readInt() {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
