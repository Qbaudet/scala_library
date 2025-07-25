package main

import models.*
import models.UserId.*
import services.Catalog
import utils.CatalogIO

import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn.readLine
import scala.util.Try

@main def runLibrarySystem(): Unit =

  val bookPath = Paths.get("src", "data", "books.json").toString
  val userPath = Paths.get("src", "data", "users.json").toString
  // Load initial data from JSON
  val books = CatalogIO.loadBooks(bookPath)
  val users = CatalogIO.loadUsers(userPath)
  var catalog = Catalog(books, users)

  println("Welcome to the Library Management System")
  val (user, updatedCatalog) = authenticateOrCreateUser(catalog, userPath)

  println(s"Authenticated as ${user.name} (ID: ${user.id.value})")

  // Route to menu based on user role
  user match
    case _: Member => memberMenu(user.asInstanceOf[Member], updatedCatalog)
    case _: Librarian => librarianMenu(user.asInstanceOf[Librarian], updatedCatalog, bookPath)
    case _: Faculty => facultyMenu(user.asInstanceOf[Faculty], updatedCatalog, userPath)

def authenticateOrCreateUser(catalog: Catalog, userPath: String): (User, Catalog) =
  print("Enter your user ID: ")
  val input = readLine().trim
  val idOpt = Try(input.toInt).toOption.map(UserId(_))

  idOpt.flatMap(id => catalog.users.find(_.id == id)) match
    case Some(existingUser) =>
      println(s"Welcome back, ${existingUser.name}!")
      (existingUser, catalog)

    case None =>
      println("User not found. Let's create a new one.")
      val newUser = createUserInteractive(input)
      catalog.addUser(newUser) match
        case Right(updatedCatalog) =>
          CatalogIO.saveUsers(updatedCatalog, userPath)
          println("User successfully created.")
          (newUser, updatedCatalog)
        case Left(error) =>
          println(s"Failed to create user: $error")
          sys.exit(1)


def createUserInteractive(idStr: String): User =
  val id = Try(idStr.toInt).getOrElse {
    println("Invalid ID format.")
    sys.exit(1)
  }
  val userId = UserId(id)

  print("Enter the name: ")
  val name = readLine().trim

  println("Choose the role: [1] Member  [2] Librarian  [3] Faculty")
  val role = readLine().trim

  role match
    case "1" => Member(name, userId)
    case "2" => Librarian(name, userId)
    case "3" => Faculty(name, userId)
    case _ =>
      println("Invalid role selected.")
      sys.exit(1)


def memberMenu(user: Member, catalog: Catalog): Unit = {
  var continue       = true
  var currentCatalog = catalog

  while continue do
    println("\n== Member Menu ==")
    println("1. Browse books")
    println("2. Borrow a book")
    println("3. Return a book")
    println("4. Exit")

    readLine("Choose an option: ").trim match
      // 1. BROWSE
      case "1" =>
        // On récupère la liste des livres disponibles
        val available = Await.result(currentCatalog.listAvailableBooks, 5.seconds)
        if available.isEmpty then
          println("No books available at the moment.")
        else
          println("\nAvailable books:")
          available.foreach { book =>
            println(s"- ISBN: ${book.ISBN.value} | ${book.title} by ${book.authors.mkString(", ")} (${book.publicationyear})")
          }

      // 2. BORROW
      case "2" =>
        val isbn = readLine("Enter ISBN to borrow: ").trim
        currentCatalog.books.find(_.ISBN.value == isbn) match
          case None =>
            println(s"No book with ISBN '$isbn' found.")
          case Some(book) =>
            // On enregistre la transaction
            currentCatalog.recordTransaction(Transaction(book, user, LocalDateTime.now())) match
              case Left(err) =>
                println(s"Could not borrow book: $err")
              case Right(updated) =>
                currentCatalog = updated
                println(s"Book '${book.title}' borrowed successfully!")

      // 3. RETURN
      case "3" =>
        val isbn = readLine("Enter ISBN to return: ").trim
        // On cherche la transaction non retournée pour cet utilisateur et ISBN
        val maybeTx = currentCatalog.transactions.find { tx =>
          tx.user.id == user.id &&
            tx.book_loans.ISBN.value == isbn &&
            tx.returns.isEmpty
        }
        maybeTx match
          case None =>
            println(s"No active loan found for ISBN '$isbn'.")
          case Some(tx) =>
            // On marque la date de retour
            val returnedTx = tx.copy(returns = Some(LocalDateTime.now()))
            currentCatalog.transactions =
              returnedTx :: currentCatalog.transactions.filterNot(_ == tx)
            println(s"Book '${tx.book_loans.title}' returned. Thank you!")

      // 4. EXIT
      case "4" =>
        println("Goodbye!")
        continue = false

      case _ =>
        println("Invalid choice. Try again.")
}


def librarianMenu(user: Librarian, catalog: Catalog, bookPath: String): Unit =
  var continue = true
  var localCatalog = catalog
  while continue do
    println("\n--- Librarian Menu ---")
    println("1. Add a new book")
    println("2. Remove a book")
    println("3. Search a book")
    println("4. See a books statistics")
    println("5. See ongoing transactions")
    println("0. Exit")
    print("Select an option: ")
    readLine().trim match

      case "1" =>
        println("== Add a New Book ==")

        print("Enter ISBN (number only): ")
        val isbnInput = readLine().trim
        val maybeISBN = Try(isbnInput).toOption.map(ISBN(_))

        maybeISBN match
          case Some(isbn) =>
            print("Enter title: ")
            val title = readLine().trim

            print("Enter authors (comma-separated): ")
            val authorsInput = readLine().trim
            val authors = authorsInput.split(",").map(_.trim).toList

            print("Enter publication year: ")
            val year = readLine().trim.toIntOption.getOrElse {
              println("Invalid year.")
              return
            }

            println("Available genres: Programming, Poetry, Computer Science, Machine Learning, Other")
            print("Enter genre: ")
            val genreInput = readLine().trim

            val maybeGenre = parseGenre(genreInput)
            maybeGenre match
              case Some(genre) =>
                print("Is the book available? [y/n]: ")
                val available = readLine().trim.toLowerCase match
                  case "y" => true
                  case "n" => false
                  case _ =>
                    println("Invalid availability input. Assuming unavailable.")
                    false

                val newBook = Book_Entity(
                  ISBN = isbn,
                  title = title,
                  authors = authors,
                  publicationyear = year,
                  genre = genre,
                  availability = available
                )
                
                user.addBook(newBook, localCatalog) match
                  case Right(updatedCatalog) =>
                    localCatalog = updatedCatalog
                    CatalogIO.saveBooks(localCatalog, bookPath)
                    println("Book added successfully.")
                
                  case Left(error) =>
                    println(s"Error: $error")

              case None =>
                println("Invalid genre entered.")

          case None =>
            println("Invalid ISBN format.")



def facultyMenu(user: Faculty, catalog: Catalog, userPath: String): Unit =
  var continue = true
  var localCatalog = catalog
  while continue do
    println("\n--- Faculty Menu ---")
    println("1. Add a new user")
    println("2. Remove a user")
    println("3. View all users")
    println("0. Exit")
    print("Select an option: ")
    readLine().trim match
      case "1" =>
        val newUserTemplate = createUserInteractive("0")
        val maybeId = readLine("Enter ID for the new user: ").trim.toIntOption

        maybeId match
          case Some(validId) =>
            val newUserId = UserId(validId)

            if localCatalog.users.exists(_.id == newUserId) then
              println(s"Error: User ID ${validId} already exists. Cannot add duplicate.")
            else
              val finalUser = newUserTemplate match
                case Member(n, _)    => Member(n, newUserId)
                case Librarian(n, _) => Librarian(n, newUserId)
                case Faculty(n, _)   => Faculty(n, newUserId)

              user.addUser(finalUser, localCatalog) match
                case Right(updatedCatalog) =>
                  localCatalog = updatedCatalog
                  CatalogIO.saveUsers(localCatalog, userPath)
                  println("User added successfully.")
                case Left(error) =>
                  println(s"Error: $error")

          case None =>
            println("Invalid ID format.")

      case "2" =>
          print("Enter the ID of the user to remove: ")
          val removeId = readLine().trim.toIntOption
          removeId match
            case Some(uid) =>
              val userToDelete = UserId(uid)
              if localCatalog.users.exists(_.id == userToDelete) then
                user.removeUser(userToDelete, catalog)
                CatalogIO.saveUsers(catalog, userPath)
                println(s"User with ID ${userToDelete} removed.")
              else
                println(s"Error: User ID ${userToDelete} not found.")
            case None =>
              println("Invalid ID format.")

      case "3" =>
        println("\n--- Registered Users ---")
        val users = CatalogIO.loadUsers(userPath)
        users.foreach { u =>
          println(s"[${u.id.value}] ${u.name} (${u.getClass.getSimpleName.replace("$", "")})")
        }

      case "0" =>
        continue = false
        println("Exiting Faculty menu.")

      case _ =>
        println("Invalid option. Please select again.")

def parseGenre(input: String): Option[Genre] = input.trim.toLowerCase match
  case "programming" => Some(Genre.Programming)
  case "poetry" => Some(Genre.Poetry)
  case "computer science" => Some(Genre.ComputerScience)
  case "machine learning" => Some(Genre.MachineLearning)
  case "other" => Some(Genre.Other)
  case _ => None