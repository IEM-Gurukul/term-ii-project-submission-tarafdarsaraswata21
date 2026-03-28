# Smart Library Management System

**Term-II Individual Project | Spring 2026**

---

## Project Overview

A small/medium-scale Java application demonstrating strong Object-Oriented Design principles through a fully functional library management system.

---

## Package Structure

```
SmartLibrary/
└── src/
    ├── model/
    │   ├── LibraryItem.java       (abstract base class)
    │   ├── Book.java              (extends LibraryItem)
    │   ├── Magazine.java          (extends LibraryItem)
    │   ├── Member.java            (library member)
    │   └── BorrowRecord.java      (tracks each borrow transaction)
    ├── exception/
    │   ├── BookNotFoundException.java
    │   ├── AlreadyBorrowedException.java
    │   └── MemberNotFoundException.java
    ├── factory/
    │   └── LibraryItemFactory.java  (Factory Pattern)
    ├── strategy/
    │   ├── FineStrategy.java        (interface - Strategy Pattern)
    │   ├── RegularFineStrategy.java (Rs. 2.00/day)
    │   └── PremiumFineStrategy.java (Rs. 1.00/day, max Rs. 30)
    ├── service/
    │   ├── LibraryService.java      (Singleton Pattern - core logic)
    │   └── DataManager.java         (Java Serialization persistence)
    └── ui/
        └── Main.java                (Console menu - entry point)
└── test/
    └── service/
        └── LibraryServiceTest.java  (JUnit tests)
```

---

## OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| Abstraction | `LibraryItem` abstract class with `getDetails()` |
| Inheritance | `Book` and `Magazine` extend `LibraryItem` |
| Polymorphism | `getDetails()` behaves differently per subclass |
| Encapsulation | All fields are `private`, accessed via getters/setters |
| Exception Handling | 3 custom exceptions thrown throughout service layer |
| Collections | `HashMap` and `ArrayList` used throughout |
| File Handling | Java Serialization via `ObjectOutputStream/InputStream` |

---

## Design Patterns Used

| Pattern | Class | Purpose |
|---|---|---|
| Singleton | `LibraryService` | Ensures one shared service instance |
| Factory | `LibraryItemFactory` | Creates Book or Magazine from a type string |
| Strategy | `FineStrategy` interface | Swappable fine calculation algorithm |

---

## How to Run

1. Compile all `.java` files from the `src/` directory
2. Run `ui.Main` as the entry point
3. Follow the console menu (12 options)
4. Data is saved to `library_items.ser` and `library_members.ser` on exit

```bash
# Compile
javac -d out src/**/*.java

# Run
java -cp out ui.Main
```

---

## How to Run Tests

Add `junit-4.x.jar` to your classpath, then:

```bash
javac -cp junit.jar:src -d out test/**/*.java src/**/*.java
java  -cp junit.jar:out  org.junit.runner.JUnitCore service.LibraryServiceTest
```

---

## Persistence

Data is saved using **Java Object Serialization** to two binary files:
- `library_items.ser` — all books and magazines
- `library_members.ser` — all members and their borrow histories

These files are loaded automatically on startup and saved on exit (option 12).

---

## Fine Calculation

| Strategy | Rate | Cap |
|---|---|---|
| Regular | Rs. 2.00 per day | None |
| Premium | Rs. 1.00 per day | Rs. 30.00 max |

Switch strategies at runtime via option 11 in the menu.
