# Library Management System – User Manual

This document provides a guide on how to interact with the **Library Management System** via its **console-based menu interface**. It outlines the functionalities available to different user roles: **Member**, **Librarian**, and **Faculty**, along with illustrative **use case scenarios** and **console interaction examples**.

---

## 1. System Overview

The Library Management System is designed to manage:

- Books
- Users
- Borrowing transactions

It supports three distinct user roles, each with specific permissions:

- **Member**: Can borrow books, return books, and view available books.
- **Librarian**: Has full control over books (create, delete), can manage members (create, delete, list), and handle borrowing/returning for members.
- **Faculty**: Can view the list of members, add new members, and delete members.

---

## 2. How to Start and Navigate the System

### Start the Application

```bash
sbt run
```

### Login

```text
Welcome to the Library Management System!
Please enter your ID
```

- If your account exists, you'll be logged in automatically.
- If not, you'll be redirected to the user creation page.

> See **"Creating an account"** for more details.

### Main Menu

Once logged in, you will see a menu specific to your role. You interact by entering the number corresponding to your desired action and pressing Enter.

### Prompts for Information

For actions requiring specific input (e.g., ISBN, member name), the system will prompt you accordingly.

### Returning to Menu

After most operations, the system displays a message and returns you to your role's main menu.

---

## 3. User Roles and Use Case Examples

### 3.1 Member Actions

Members can borrow and return books, and check availability.

#### Scenario 1: Borrow a Book

**User**: Alice (ID: 1) wants to borrow *"The Lord of the Rings"*.

```text
Hello, Alice (Member)!
Member Menu:
1. View Available Books
2. Borrow a Book
3. Return a Book
4. Logout
Enter your choice (1-4): 2

--- Borrow a Book ---
Enter the ISBN of the book you wish to borrow: 978-0618053267

Processing request...
Success: Book 'The Lord of the Rings' borrowed by Member 1.
```

#### Scenario 2: Return a Book

```text
Enter your choice (1-4): 3

--- Return a Book ---
Enter the ISBN of the book you wish to return: 978-0618053267

Processing request...
Success: Book 'The Lord of the Rings' returned by Member M001.
```

#### Scenario 3: View Available Books

**User**: Bob (ID: 2) wants to see the available books.

```text
Enter your choice (1-4): 1

--- Available Books ---
- Title: "1984", Author: "George Orwell", ISBN: "978-0451524935", Available: true
- Title: "To Kill a Mockingbird", Author: "Harper Lee", ISBN: "978-0446310789", Available: true

Press Enter to continue...
```

---

### 3.2 Librarian Actions

Librarians manage books, members, and assist with transactions.

#### Scenario 4: Add a New Book

**User**: Mr. Smith (ID: 10) adds *"Dune"* to the library.

```text
Enter your choice (1-4): 1

--- Manage Books ---
1. Add New Book
2. Delete Book
3. Back to Main Menu
Enter your choice (1-3): 1

--- Add New Book ---
Enter ISBN: 978-0441013593
Enter Title: Dune
Enter Author: Frank Herbert
Enter Publication Year: 1965
Enter Genre: Science Fiction

Processing request...
Success: Book 'Dune' (ISBN: 978-0441013593) added to the catalog.
```

#### Scenario 5: Register a New Member

```text
Enter your choice (1-4): 2

--- Manage Members ---
1. Create New Member
2. Delete Member
3. List All Members
4. Back to Main Menu
Enter your choice (1-4): 1

--- Create New Member ---
Enter new Member ID (e.g., M004): M004
Enter Member's Name: Diana

Processing request...
Success: New Member 'Diana' with ID M004 created.
```

#### Scenario 6: Help Member Borrow a Book

**User**: Mr. Smith assists Charlie (ID: M003) in borrowing *"1984"*.

```text
Enter your choice (1-4): 3

--- Handle Loans for Members ---
1. Borrow Book for Member
2. Return Book for Member
3. Back to Main Menu
Enter your choice (1-3): 1

--- Borrow Book for Member ---
Enter Member ID: M003
Enter ISBN of the book: 978-0451524935

Processing request...
Success: Book '1984' borrowed by Member M003.
```

---

### 3.3 Faculty Actions

Faculty can manage members within the system.

#### Scenario 7: List All Members

**User**: Professor Jones (ID: F001) wants to list all registered users.

```text
Enter your choice (1-3): 3
Enter your Faculty ID: F001

--- All Registered Members ---
- ID: M001, Name: Alice, Role: Member
- ID: M002, Name: Bob, Role: Member
- ID: M003, Name: Charlie, Role: Member
- ID: L001, Name: Mr. Smith, Role: Librarian
- ID: F001, Name: Professor Jones, Role: Faculty

Press Enter to continue...
```

#### Scenario 8: Delete a Member

**User**: Professor Jones removes student M004.

```text
Enter your choice (1-4): 2

--- Delete Member ---
Enter Member ID to delete: M004

Processing request...
Success: Member M004 deleted.
```

---

## 4. Error Handling and System Messages

- **Success Messages**:  
  *"Book 'Dune' added."*

- **Error Messages**:  
  *"Error: Book with ISBN '...' is currently unavailable."*  
  *"Error: Cannot delete member '...' as they currently have borrowed books."*

- **Invalid Input**:  
  If you enter an invalid menu choice or wrong data type, the system will prompt you to try again.

---

## 5. Exiting the System

To exit the application:

- Choose **Exit** from your current role's menu.
- This will shut down the app