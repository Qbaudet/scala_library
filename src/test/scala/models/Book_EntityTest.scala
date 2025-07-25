package models

import models._
import org.scalatest.funsuite.AnyFunSuite

class BookEntityTest extends AnyFunSuite {

  test("Book_Entity should serialize and deserialize correctly") {
    val book1 = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)

    // Sérialisation
    val json = upickle.default.write(book1)

    // Désérialisation
    val deserializedBook = upickle.default.read[Book_Entity](json)

    assert(deserializedBook == book1) // Vérifie que la désérialisation fonctionne correctement
  }

  test("Book_Entity genre should convert to lowercase correctly") {
    val book1 = Book_Entity(ISBN("12345"), "Scala Basics", List("Author A"), 2020, Genre.Programming, true)

    assert(book1.genre.toLowerCase == "programming") // Vérifie que le genre est bien converti en minuscules
  }
}
