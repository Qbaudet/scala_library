package models

import upickle.default.{ReadWriter, macroRW, readwriter}

/**
 * Représente un ISBN dans le système, utilisant un type opaque pour garantir l'encapsulation du type.
 * Le type opaque `ISBN` est une chaîne de caractères (`String`), mais est traité de manière distincte pour
 * éviter toute confusion avec des chaînes de caractères classiques.
 *
 * @param value Le contenu de l'ISBN sous forme de chaîne de caractères.
 */

object ISBN:
  opaque type ISBN = String

  /**
   * Crée une instance de type `ISBN` à partir d'une chaîne de caractères.
   * Cette méthode permet de créer un ISBN à partir d'une chaîne, tout en encapsulant cette chaîne
   * dans le type opaque `ISBN`.
   *
   * @param value La chaîne de caractères représentant l'ISBN.
   * @return Un `ISBN` encapsulant la chaîne fournie.
   */
  def apply(value: String): ISBN = value

  /**
   * Extension de la classe `ISBN` pour ajouter des méthodes supplémentaires permettant de manipuler
   * l'ISBN.
   */
  extension (isbn: ISBN)

    /**
     * Retourne la valeur de l'ISBN sous forme de chaîne de caractères.
     *
     * @return La chaîne de caractères représentant l'ISBN.
     */
    def value: String = isbn

    /**
     * Retourne la valeur de l'ISBN après avoir supprimé les espaces blancs autour.
     *
     * @return Un nouvel `ISBN` avec la valeur nettoyée des espaces blancs.
     */
    def trimValue: ISBN = isbn.value.trim

    /**
     * Retourne la valeur de l'ISBN convertie en minuscules.
     *
     * @return Un nouvel `ISBN` avec la valeur convertie en minuscules.
     */
    def toLowerCase: ISBN = isbn.value.toLowerCase

  /**
   * Sérialisation et désérialisation pour le type opaque `ISBN` avec `uPickle`.
   * Utilise la méthode `bimap` pour convertir un `ISBN` en chaîne de caractères et inversement.
   *
   * Cette conversion permet de sérialiser un `ISBN` en une chaîne de caractères et de le désérialiser
   * à partir d'une chaîne en utilisant `uPickle`.
   */
  given rwISBN: ReadWriter[ISBN] =
    readwriter[String].bimap(ISBN(_), _.value)
