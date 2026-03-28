# PCCCS495 – Term II Project

## Project Title

Smart Library Management System

---

## Problem Statement (max 150 words)

Managing a library manually is time-consuming and error-prone. Librarians struggle to track which books are available, which are borrowed, who borrowed them, and when they are due. There is no easy way to search for a book, record fines for late returns, or keep member records organised. Duplicate borrowing of the same item, lost return records, and untracked overdue fines are common problems in manual systems. This project provides a well-structured, object-oriented Java application that handles book inventory, member management, borrowing and return operations, and automatic fine calculation in a clean and reliable way.

---

## Target User

- Librarians who need to manage book inventory and member records
- Library members who borrow and return books
- Administrators who oversee borrow history and fine management

---

## Core Features

- Add, remove, and search books and magazines by title, author, or ID
- Register and manage library members
- Issue (borrow) items to members with automatic due date tracking (14 days)
- Return items and automatically calculate overdue fines via Strategy Pattern
- View full borrowing history for any member
- Switch fine strategy at runtime (Regular: Rs. 2.00/day | Premium: Rs. 1.00/day, max Rs. 30)
- File-based persistence using Java Serialization — data saved to .ser files on exit and reloaded on startup
- Custom exception handling for invalid operations (e.g. borrowing an already-borrowed book)

---

## OOP Concepts Used

- **Abstraction:** `LibraryItem` is an abstract class with abstract method `getDetails()` — subclasses must provide their own implementation
- **Inheritance:** `Book` and `Magazine` both extend `LibraryItem`, reusing common fields (itemId, title, author, isAvailable) via `super()`
- **Polymorphism:** `getDetails()` is overridden in both `Book` and `Magazine` — same method name, different output at runtime
- **Exception Handling:** Three custom checked exceptions (`BookNotFoundException`, `AlreadyBorrowedException`, `MemberNotFoundException`) extend `Exception` and are thrown throughout the service layer
- **Collections / Threads:** `HashMap<String, LibraryItem>` and `HashMap<String, Member>` used in `LibraryService` for O(1) ID-based lookup; `ArrayList<BorrowRecord>` used in `Member` for borrow history

---

## Proposed Architecture Description

The project follows a clean six-package layered architecture:

- **model/** — Core entity classes: `LibraryItem` (abstract), `Book`, `Magazine`, `Member`, `BorrowRecord`. All implement `Serializable` for file persistence.
- **exception/** — Custom checked exceptions: `BookNotFoundException`, `AlreadyBorrowedException`, `MemberNotFoundException`.
- **factory/** — `LibraryItemFactory` implements the **Factory Pattern** — creates `Book` or `Magazine` objects from a type string, centralising object creation.
- **strategy/** — `FineStrategy` interface implements the **Strategy Pattern** with two concrete classes: `RegularFineStrategy` (Rs. 2.00/day) and `PremiumFineStrategy` (Rs. 1.00/day, capped at Rs. 30). Swappable at runtime.
- **service/** — `LibraryService` implements the **Singleton Pattern** — one shared instance manages all items, members, and borrow records using HashMaps. `DataManager` handles Java Serialization for save/load.
- **ui/** — `Main.java` provides a 12-option console menu connecting all layers.

---

## How to Run

**Compile:**
```bash
javac -d out src/model/*.java src/exception/*.java src/factory/*.java src/strategy/*.java src/service/*.java src/ui/*.java
```

**Run:**
```bash
java -cp out ui.Main
```

**Run Tests (JUnit 4 required):**
```bash
javac -cp junit.jar:src -d out test/service/LibraryServiceTest.java src/**/*.java
java  -cp junit.jar:out  org.junit.runner.JUnitCore service.LibraryServiceTest
```

> Data is automatically saved to `library_items.ser` and `library_members.ser` when you exit via option 12, and reloaded on the next startup.

---

## Git Discipline Notes

Minimum 10 meaningful commits required.

| Day | Commit Message |
|-----|---------------|
| 1 | Initialize project structure with package folders and LibraryItem abstract class |
| 2 | Add Book and Magazine classes extending LibraryItem with @Override getDetails() |
| 3 | Add Member class with borrow history ArrayList and BorrowRecord with LocalDate |
| 4 | Add custom exceptions BookNotFoundException, AlreadyBorrowedException, MemberNotFoundException |
| 4 | Add LibraryItemFactory using Factory design pattern |
| 5 | Add FineStrategy interface and RegularFineStrategy and PremiumFineStrategy implementations |
| 6 | Add LibraryService with Singleton pattern and HashMap storage for items and members |
| 7 | Add issueItem() and returnItem() with fine calculation using FineStrategy |
| 8 | Add DataManager with Java Serialization for full library state persistence |
| 9 | Add Main.java console menu with 12 options — full system integration |
| 10 | Add JUnit test suite with 16 test cases covering all major functionality |
