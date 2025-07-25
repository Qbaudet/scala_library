
# Library Management System – User Manual

This document provides a guide on how to interact with the **Library Management System** via its **console-based menu interface**. It outlines the functionalities available to different user roles: **Member**, **Librarian**, and **Faculty**, along with illustrative **use case scenarios** and **console interaction examples**.

---

## 1. System Overview

The Library Management System is designed to manage:
- Books
- Users
- Borrowing transactions

It supports three distinct user roles, each with specific permissions:
- **Member**: Can borrow books, return books, browse catalogs, and receive recommendations.
- **Librarian**: Has full control over books (create, delete), can manage transactions, and view statistics.
- **Faculty**: Can manage users (create, delete, list).

---

## 2. How to Start and Navigate the System

### Start the Application

```bash
sbt run
```

### Login

```text
Welcome to the Library Management System! Enter your user ID:
```


- If your account exists, you'll be logged in automatically
- If not, you'll be redirected to the user creation page

### Main Menu
After logging in, you will see a menu specific to your role.

---

## 3. Role-Specific Features

### 3.1 Member Menu

Members have access to:

1. **Browse books**
2. **Borrow a book**
3. **Return a book**
4. **Get recommendations**

#### Example: Borrowing a Book

```text
== Member Menu ==
1. Browse books
2. Borrow a book
3. Return a book
4. Get recommendations
5. Exit

Choose an option: 2 Enter ISBN to borrow: 978-0618053267 Book 'The Lord of the Rings' successfully borrowed!
```

#### Example: Getting Recommendations

```text
Choose an option: 4
Here are some recommendations based on your previous loans:
- Dune by Frank Herbert (1965) Genre: Science Fiction ISBN: 978-0441013593
```

### 3.2 Librarian Menu

Librarians can access:

1. **Add a new book**
2. **Remove a book**
3. **Search for a book**
4. **View available books**
5. **View statistics**
6. **View ongoing transactions**

#### Example: Adding a Book

```text
--- Librarian Menu ---
1. Add a new book
2. Remove a book
3. Search for a book
4. View available books
5. View statistics
6. View ongoing transactions
7. Exit

Select an option: 1
== Add a New Book == Enter ISBN (numbers only): 978-0441013593 Enter title: Dune Enter authors (comma-separated): Frank Herbert Enter publication year: 1965 Available genres: Programming, Poetry, Computer Science, Machine Learning, Other Enter genre: Other Is the book available? [y/n]: y
Book successfully added.
```

#### Example: Viewing Statistics

```text
Select option: 5
General statistics: Total books: 150 Available books: 120 Unavailable books: 30 Available genres: Programming, Poetry, Computer Science, Machine Learning, Other Average publication year: 1998 Most common genre: Computer Science
```

### 3.3 Faculty Menu

Faculty members can:

1. **Add a new user**
2. **Remove a user**
3. **View all users**

#### Example: Adding a User

```text
--- Faculty Menu ---
1. Add a new user
2. Remove a user
3. View all users
4. Exit

Select an option: 1
Enter ID for new user: 5 Enter name: John Smith Choose role: [1] Member [2] Librarian [3] Faculty 1
User successfully added.
```

---

## 4. Error Handling

The system displays clear messages for common errors:

- **Input Errors**:
    - "Invalid ID format."
    - "Invalid genre entered."

- **Transaction Errors**:
    - "Error: Book with ISBN '...' not found."
    - "Error: No active loan found for ISBN '...'"

- **Authentication Errors**:
    - "Error: User ID '...' already exists."
    - "Error: User with ID '...' not found."

---

## 5. Exiting the System

To exit the application:
- Select option "0. Exit" from any menu
- The system will save all changes before closing
