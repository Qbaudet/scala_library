package services

import models.*
import services.*
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDateTime
import scala.concurrent.Await
import scala.concurrent.duration.*

class CatalogTest extends AnyFunSuite {

  // Initialisation d'un catalogue
  val book1: Book_Entity = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)
  val book2: Book_Entity = Book_Entity(ISBN("67890"), "Advanced Scala", List("Author B"), 2018, Genre.Programming, false)
  val book3: Book_Entity = Book_Entity(ISBN("11111"), "Machine Learning", List("Author C"), 2019, Genre.MachineLearning, true)
  val user1: User = Member("Alice", UserId(1))
  val catalog: Catalog = Catalog(List(book1, book2), List(user1))

  // Test de l'ajout d'un livre
  test("addBook should add a valid book to the catalog") {
    val newBook = Book_Entity(ISBN("99999"), "Functional Programming", List("Author D"), 2022, Genre.Programming, true)
    val result = catalog.addBook(newBook)

    result match {
      case Right(updatedCatalog) =>
        assert(updatedCatalog.books.contains(newBook))
      case Left(error) =>
        fail(s"Book was not added: $error")
    }
  }

  // Test de la suppression d'un livre
  test("removeBook should remove a book by ISBN") {
    val updatedCatalog = catalog.removeBook(ISBN("12345"))
    assert(!updatedCatalog.books.exists(_.ISBN == ISBN("12345")))
  }

  // Test de la liste des livres disponibles
  test("listAvailableBooks should return only available books") {
    val book1: Book_Entity = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)
    val book2: Book_Entity = Book_Entity(ISBN("67890"), "Advanced Scala", List("Author B"), 2018, Genre.Programming, false)
    val catalog = Catalog(List(book1, book2), List())

    val availableBooksFuture = catalog.listAvailableBooks
    val availableBooks = Await.result(availableBooksFuture, 10.seconds)

    assert(availableBooks.size == 1)
    assert(availableBooks.head == book1)
  }

  // Test de l'ajout d'un utilisateur
  test("addUser should add a valid user to the catalog") {
    val newUser = Member("Bob", UserId(2))
    val result = catalog.addUser(newUser)

    result match {
      case Right(updatedCatalog) =>
        assert(updatedCatalog.users.contains(newUser))
      case Left(error) =>
        fail(s"User was not added: $error")
    }
  }

  // Test de la suppression d'un utilisateur
  test("removeUser should remove a user by ID") {
    val updatedCatalog = catalog.removeUser(UserId(1))
    assert(!updatedCatalog.users.exists(_.id == UserId(1)))
  }

  // Test de la recherche
  test("search should return books matching the query") {
    val query = "Scala"
    val resultsFuture = catalog.search(query)
    val results = Await.result(resultsFuture, 10.seconds)

    assert(results.size == 2)
    assert(results.contains("Book found: Scala Basics by Author A"))
    assert(results.contains("Book found: Advanced Scala by Author B"))
  }

  // Test des statistiques
  test("statistics should return correct statistics") {
    val statistics = catalog.statistics()

    assert(statistics.contains("Total books: 2"))
    assert(statistics.contains("Available books: 1"))
    assert(statistics.contains("Unavailable books: 1"))
  }

  // Test des recommandations pour un utilisateur
  test("recommendBooksFor should return books based on user's preferences (genre or author)") {
    val book1 = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)
    val book2 = Book_Entity(ISBN("67890"), "Advanced Scala", List("Author B"), 2018, Genre.Programming, true)
    val book3 = Book_Entity(ISBN("11111"), "Machine Learning", List("Author C"), 2019, Genre.MachineLearning, true)
    val book4 = Book_Entity(ISBN("22222"), "Deep Learning", List("Author D"), 2021, Genre.MachineLearning, true)

    val user1 = Member("Alice", UserId(1))
    val catalog = Catalog(List(book1, book2, book3, book4), List(user1))

    // L'utilisateur a emprunté book1 (Genre.Programming) et book3 (Genre.MachineLearning)
    val transaction1 = Transaction(book1, user1, LocalDateTime.now())
    val transaction2 = Transaction(book3, user1, LocalDateTime.now())
    val updatedCatalog = catalog.recordTransaction(transaction1).getOrElse(catalog)
    val furtherUpdatedCatalog = updatedCatalog.recordTransaction(transaction2).getOrElse(updatedCatalog)

    val recommendationsFuture = furtherUpdatedCatalog.recommendBooksFor(user1)
    val recommendations = Await.result(recommendationsFuture, 10.seconds)

    // Book2 (Programming) et book4 (MachineLearning) sont des recommandations valides
    assert(recommendations.size == 2)
    assert(recommendations.contains(book2))
    assert(recommendations.contains(book4))
    assert(!recommendations.contains(book1)) 
    assert(!recommendations.contains(book3))
  }

}
