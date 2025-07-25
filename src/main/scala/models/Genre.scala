package models

import upickle.default.{ReadWriter, readwriter}

/**
 * Représente un genre de livre dans le catalogue.
 * L'énumération `Genre` contient les types de genres de livres disponibles.
 *
 * Les genres définis ici sont :
 * - `Programming` : Programmation
 * - `Poetry` : Poésie
 * - `ComputerScience` : Informatique
 * - `MachineLearning` : Apprentissage Automatique
 * - `Other` : Autres genres non spécifiés
 */

enum Genre:
  case Programming, Poetry, ComputerScience, MachineLearning, Other

  /**
   * Retourne une chaîne de caractères représentant le genre.
   * Cette méthode est redéfinie pour retourner des noms conviviaux (par exemple, "Programming").
   *
   * @return Une chaîne de caractères représentant le genre sous forme lisible.
   */

  override def toString: String = this match
    case Programming => "Programming"
    case Poetry => "Poetry"
    case ComputerScience => "Computer Science"
    case MachineLearning => "Machine Learning"
    case Other => "Other"

object Genre:

  /**
   * Sérialisation et désérialisation du genre.
   * Utilise `uPickle` pour convertir le genre en chaîne de caractères et vice-versa.
   * La conversion est effectuée avec `bimap`, ce qui permet de convertir un genre en une chaîne de caractères
   * puis de reconvertir cette chaîne en un genre valide.
   */

  given rwGenre: ReadWriter[Genre] = readwriter[String].bimap(
    _.toString,
    {
      case "Programming" => Genre.Programming
      case "Poetry" => Genre.Poetry
      case "Computer Science" => Genre.ComputerScience
      case "Machine Learning" => Genre.MachineLearning
      case "Other" => Genre.Other
      case _ => throw new Exception("Invalid genre")
    }
  )

  /**
   * Extension pour la classe `Genre` permettant d'effectuer des opérations supplémentaires sur les genres.
   */

  extension (genre: Genre)

    /**
     * Convertit le genre en une chaîne de caractères en minuscules.
     *
     * @return La représentation en minuscules du genre.
     */
    def toLowerCase: String = genre.toString.toLowerCase

    /**
     * Supprime les espaces blancs au début et à la fin du nom du genre.
     *
     * @return La chaîne de caractères du genre après suppression des espaces.
     */
    def trim: String = genre.toString.trim


