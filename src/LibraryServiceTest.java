package service;

import exception.AlreadyBorrowedException;
import exception.BookNotFoundException;
import exception.MemberNotFoundException;
import model.BorrowRecord;
import model.LibraryItem;
import model.Member;
import org.junit.Before;
import org.junit.Test;
import strategy.PremiumFineStrategy;
import strategy.RegularFineStrategy;

import java.util.List;

import static org.junit.Assert.*;

public class LibraryServiceTest {

    private LibraryService service;

    // ── SETUP ──
    // @Before runs before EVERY single test method
    // We reset the singleton so each test starts with a clean slate
    @Before
    public void setUp() {
        service = LibraryService.getInstance();
        service.getItems().clear();
        service.getMembers().clear();
        service.setFineStrategy(new RegularFineStrategy());
    }

    // ══════════════════════════════
    // ITEM TESTS
    // ══════════════════════════════

    @Test
    public void testAddBook() {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        List<LibraryItem> items = service.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Clean Code", items.get(0).getTitle());
    }

    @Test
    public void testAddMagazine() {
        service.addItem("MAGAZINE", "M01", "Tech Monthly", "Editor", "12", "March");
        List<LibraryItem> items = service.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Tech Monthly", items.get(0).getTitle());
    }

    @Test
    public void testSearchItemByIdFound() throws BookNotFoundException {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        LibraryItem item = service.searchItemById("B01");
        assertNotNull(item);
        assertEquals("Clean Code", item.getTitle());
    }

    @Test(expected = BookNotFoundException.class)
    public void testSearchItemByIdNotFound() throws BookNotFoundException {
        service.searchItemById("INVALID_ID");
    }

    @Test
    public void testSearchItemsByTitle() {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        service.addItem("BOOK", "B02", "Clean Architecture", "Robert Martin", "978-1", "Technology");
        List<LibraryItem> results = service.searchItemsByTitle("Clean");
        assertEquals(2, results.size());
    }

    @Test
    public void testRemoveItem() throws BookNotFoundException {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        service.removeItem("B01");
        assertEquals(0, service.getAllItems().size());
    }

    @Test(expected = BookNotFoundException.class)
    public void testRemoveItemNotFound() throws BookNotFoundException {
        service.removeItem("INVALID_ID");
    }

    // ══════════════════════════════
    // MEMBER TESTS
    // ══════════════════════════════

    @Test
    public void testAddMember() {
        service.addMember("M01", "Saraswata", "s@email.com");
        List<Member> members = service.getAllMembers();
        assertEquals(1, members.size());
        assertEquals("Saraswata", members.get(0).getName());
    }

    @Test
    public void testGetMemberFound() throws MemberNotFoundException {
        service.addMember("M01", "Saraswata", "s@email.com");
        Member m = service.getMember("M01");
        assertNotNull(m);
        assertEquals("Saraswata", m.getName());
    }

    @Test(expected = MemberNotFoundException.class)
    public void testGetMemberNotFound() throws MemberNotFoundException {
        service.getMember("INVALID_ID");
    }

    @Test
    public void testRemoveMember() throws MemberNotFoundException {
        service.addMember("M01", "Saraswata", "s@email.com");
        service.removeMember("M01");
        assertEquals(0, service.getAllMembers().size());
    }

    // ══════════════════════════════
    // BORROW / RETURN TESTS
    // ══════════════════════════════

    @Test
    public void testIssueItemSuccess()
            throws MemberNotFoundException, BookNotFoundException, AlreadyBorrowedException {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        service.addMember("M01", "Saraswata", "s@email.com");

        BorrowRecord record = service.issueItem("M01", "B01");

        assertNotNull(record);
        assertFalse(service.searchItemById("B01").isAvailable());
        assertEquals(1, service.getMember("M01").getBorrowHistory().size());
    }

    @Test(expected = AlreadyBorrowedException.class)
    public void testIssueAlreadyBorrowedItem()
            throws MemberNotFoundException, BookNotFoundException, AlreadyBorrowedException {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        service.addMember("M01", "Saraswata", "s@email.com");
        service.addMember("M02", "Rahul", "r@email.com");

        service.issueItem("M01", "B01");
        service.issueItem("M02", "B01"); // should throw AlreadyBorrowedException
    }

    @Test
    public void testReturnItemOnTime()
            throws MemberNotFoundException, BookNotFoundException, AlreadyBorrowedException {
        service.addItem("BOOK", "B01", "Clean Code", "Robert Martin", "978-0", "Technology");
        service.addMember("M01", "Saraswata", "s@email.com");

        service.issueItem("M01", "B01");
        double fine = service.returnItem("M01", "B01");

        assertEquals(0.0, fine, 0.001);
        assertTrue(service.searchItemById("B01").isAvailable());
    }

    // ══════════════════════════════
    // STRATEGY TESTS
    // ══════════════════════════════

    @Test
    public void testRegularFineStrategy() {
        RegularFineStrategy strategy = new RegularFineStrategy();
        assertEquals(10.0, strategy.calculateFine(5), 0.001);
        assertEquals(0.0,  strategy.calculateFine(0), 0.001);
        assertEquals(0.0,  strategy.calculateFine(-1), 0.001);
    }

    @Test
    public void testPremiumFineStrategyUnderCap() {
        PremiumFineStrategy strategy = new PremiumFineStrategy();
        assertEquals(10.0, strategy.calculateFine(10), 0.001);
    }

    @Test
    public void testPremiumFineStrategyCapped() {
        PremiumFineStrategy strategy = new PremiumFineStrategy();
        assertEquals(30.0, strategy.calculateFine(50), 0.001);
    }

    @Test
    public void testSwitchFineStrategy() {
        service.setFineStrategy(new PremiumFineStrategy());
        assertEquals("Premium Member Fine (Rs. 1.00 per day, max Rs. 30.00)",
                service.getFineStrategy().getStrategyName());
    }

    // ══════════════════════════════
    // FACTORY TESTS
    // ══════════════════════════════

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryUnknownType() {
        service.addItem("NEWSPAPER", "N01", "Times", "Editor", "x", "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryInvalidIssueNumber() {
        service.addItem("MAGAZINE", "M01", "Tech", "Ed", "notANumber", "March");
    }
}
