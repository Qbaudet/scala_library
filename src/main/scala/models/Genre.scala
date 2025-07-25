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
 * - `Fiction`: Fiction générale
 * - `ScienceFiction`: Science-fiction
 * - `Fantasy`: Fantasy
 * - `History`: Histoire
 * - `Biography`: Biographie
 * - `Other` : Autres genres non spécifiés
 */

enum Genre:
  case Programming, Poetry, ComputerScience, MachineLearning,
  Fiction, ScienceFiction, Fantasy, History, Biography, Other

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
    case Fiction => "Fiction"
    case ScienceFiction => "Science Fiction"
    case Fantasy => "Fantasy"
    case History => "History"
    case Biography => "Biography"
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
      case "ComputerScience" => Genre.ComputerScience
      case "Computer Science" => Genre.ComputerScience
      case "MachineLearning" => Genre.MachineLearning
      case "Machine Learning" => Genre.MachineLearning
      case "Fiction" => Genre.Fiction
      case "ScienceFiction" => Genre.ScienceFiction
      case "Science Fiction" => Genre.ScienceFiction
      case "Fantasy" => Genre.Fantasy
      case "History" => Genre.History
      case "Biography" => Genre.Biography
      case "Other" => Genre.Other
      case unknown => throw new Exception(s"Invalid genre: $unknown")
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