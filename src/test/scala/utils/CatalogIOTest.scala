package utils

import models._
import services._
import org.scalatest.funsuite.AnyFunSuite
import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.util.Using

class CatalogIOTest extends AnyFunSuite {

  // Test de la sauvegarde des livres dans un fichier
  test("saveBooks should save books to a file") {
    val book1 = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)
    val book2 = Book_Entity(ISBN("67890"), "Advanced Scala", List("Author B"), 2018, Genre.Programming, false)
    val catalog = Catalog(List(book1, book2), List())

    val tempPath = Paths.get("src", "test", "resources", "test_books.json")
    CatalogIO.saveBooks(catalog, tempPath.toString)

    // Verify file was created
    assert(Files.exists(tempPath), s"File $tempPath was not created.")

    // Read file content safely
    val fileContent = Using(Source.fromFile(tempPath.toFile))(_.mkString).get
    assert(fileContent.contains("Scala Basics"))
    assert(fileContent.contains("Advanced Scala"))

    // Delete safely after ensuring file is closed
    Files.delete(tempPath)
  }

  // Test de la sauvegarde des utilisateurs dans un fichier
  test("saveUsers should save users to a file") {
    val user1 = Member("Alice", UserId(1))
    val catalog = Catalog(List(), List(user1))

    val tempFile = Paths.get("src", "test", "resources", "test_users.json").toString
    CatalogIO.saveUsers(catalog, tempFile)

    // Vérifie que le fichier a bien été créé
    assert(Files.exists(Paths.get(tempFile)), s"File $tempFile was not created.")

    // Vérifie le contenu du fichier
    val fileContent = Using(Source.fromFile(tempFile)) { source => source.mkString}.get // get or handle error
    assert(fileContent.contains("Alice"))

    Thread.sleep(100)

    // Supprime le fichier temporaire après le test
    Files.delete(Paths.get(tempFile))
  }

  // Test du chargement des livres depuis un fichier
  test("loadBooks should load books from a file") {
    val book1 = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)
    val catalog = Catalog(List(book1), List())

    val tempFile = Paths.get("src", "test", "resources", "test_books.json").toString
    CatalogIO.saveBooks(catalog, tempFile)

    // Vérifie que le fichier a bien été créé avant de le charger
    assert(Files.exists(Paths.get(tempFile)), s"File $tempFile does not exist.")

    // Charger les livres depuis le fichier
    val loadedBooks = CatalogIO.loadBooks(tempFile)

    assert(loadedBooks.size == 1)
    assert(loadedBooks.head.title == "Scala Basics")

    Thread.sleep(100)

    // Supprime le fichier temporaire après le test
    Files.delete(Paths.get(tempFile))
  }

  // Test du chargement des utilisateurs depuis un fichier
  test("loadUsers should load users from a file") {
    val user1 = Member("Alice", UserId(1))
    val catalog = Catalog(List(), List(user1))

    val tempFile = Paths.get("src", "test", "resources", "test_users.json").toString
    CatalogIO.saveUsers(catalog, tempFile)

    // Vérifie que le fichier a bien été créé avant de le charger
    assert(Files.exists(Paths.get(tempFile)), s"File $tempFile does not exist.")

    // Charger les utilisateurs depuis le fichier
    val loadedUsers = CatalogIO.loadUsers(tempFile)

    assert(loadedUsers.size == 1)
    assert(loadedUsers.head.name == "Alice")

    Thread.sleep(100)

    // Supprime le fichier temporaire après le test
    Files.delete(Paths.get(tempFile))
  }
}
