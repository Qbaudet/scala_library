package models

import ISBN.ISBN
import models.Book_Entity
import upickle.default.{ReadWriter, macroRW}

/**
 * Représente un livre dans le catalogue.
 * Cette classe contient toutes les informations pertinentes sur un livre,
 * telles que l'ISBN, le titre, les auteurs, l'année de publication, le genre et la disponibilité.
 *
 * @param ISBN Le code ISBN unique du livre.
 * @param title Le titre du livre.
 * @param authors La liste des auteurs du livre.
 * @param publicationyear L'année de publication du livre.
 * @param genre Le genre du livre (par exemple, "Programmation", "Science-fiction", etc.).
 * @param availability Indique si le livre est disponible dans le catalogue.
 */

case class Book_Entity(
  ISBN: ISBN,
  title: String,
  authors: List[String],
  publicationyear: Int,
  genre: Genre,
  availability: Boolean
  )

/**
 * Companion object pour la classe `Book_Entity`.
 * Contient la sérialisation et la désérialisation des instances de `Book_Entity`
 * à l'aide de la bibliothèque uPickle.
 */
object Book_Entity:
  /**
   * Sérialiseur et désérialiseur pour la classe `Book_Entity`.
   * On utilise la macro `macroRW` de la bibliothèque uPickle pour générer automatiquement
   * les méthodes de sérialisation et de désérialisation.
   */
  given rwBook: ReadWriter[Book_Entity] = macroRW
