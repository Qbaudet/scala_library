package services

import models.ISBN.ISBN
import models.UserId.UserId
import models.{Book_Entity, Transaction, User}
import utils.Validators

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
 * Représente un catalogue de livres et d'utilisateurs dans le système.
 * La classe `Catalog` gère les livres, les utilisateurs, ainsi que les transactions liées aux prêts et réservations.
 * Elle fournit également des méthodes pour ajouter, retirer, rechercher des livres et des utilisateurs,
 * ainsi que pour effectuer des statistiques et des recommandations.
 *
 * @param books Liste des livres présents dans le catalogue.
 * @param users Liste des utilisateurs du catalogue.
 * @param transactions Liste des transactions effectuées (prêts et réservations).
 */
case class Catalog(
  var books: List[Book_Entity],
  var users: List[User],
  var transactions: List[Transaction] = Nil
  ):

  /**
   * Liste les livres disponibles dans le catalogue.
   * Cette méthode renvoie un `Future` contenant une liste de livres disponibles (c'est-à-dire avec `availability = true`).
   *
   * @return Un `Future` contenant la liste des livres disponibles.
   */
  def listAvailableBooks: Future[List[Book_Entity]] = Future {
    books.filter(_.availability)
  }

  /**
   * Ajoute un livre au catalogue après validation.
   * Si le livre est valide, il est ajouté à la liste des livres du catalogue.
   *
   * @param book Le livre à ajouter au catalogue.
   * @return Un `Either` avec une erreur en cas de validation échouée ou un `Catalog` mis à jour en cas de succès.
   */
  def addBook(book: Book_Entity): Either[String, Unit] =
    Validators.validateBook(book).map { validBook =>
      this.books = validBook :: this.books
    }



  /**
   * Retire un livre du catalogue en fonction de son ISBN.
   * Cette méthode renvoie un nouveau catalogue sans le livre spécifié.
   *
   * @param isbn Le code ISBN du livre à retirer du catalogue.
   * @return Un nouveau `Catalog` sans le livre spécifié.
   */
  def removeBook(isbn: ISBN): Catalog =
    copy(books = books.filterNot(_.ISBN == isbn))

  /**
   * Ajoute un utilisateur au catalogue après validation.
   * Si l'utilisateur est valide, il est ajouté à la liste des utilisateurs du catalogue.
   *
   * @param user L'utilisateur à ajouter au catalogue.
   * @return Un `Either` avec une erreur en cas de validation échouée ou un `Catalog` mis à jour en cas de succès.
   */
  def addUser(user: User): Either[String, Catalog] =
    Validators.validateUser(user).map { validUser =>
      copy(users = validUser :: users)
  }

  /**
   * Retire un utilisateur du catalogue en fonction de son identifiant.
   * Cette méthode renvoie un nouveau catalogue sans l'utilisateur spécifié.
   *
   * @param userId L'identifiant de l'utilisateur à retirer.
   * @return Un nouveau `Catalog` sans l'utilisateur spécifié.
   */
  def removeUser(userId: UserId): Catalog =
    copy(users = users.filterNot(_.id == userId))

  /**
   * Enregistre une transaction de prêt ou de réservation de livre.
   * Cette méthode vérifie la validité de la transaction et l'ajoute au catalogue si elle est valide.
   *
   * @param tx La transaction à enregistrer.
   * @return Un `Either` avec une erreur en cas de validation échouée ou un `Catalog` mis à jour en cas de succès.
   */
  def recordTransaction(tx: Transaction): Either[String, Catalog] =
    Validators.validateTransaction(tx.book_loans, tx.user, this, tx.timestamp, tx.returns, tx.reservation)
    .map(validTx =>
      copy(transactions = validTx :: transactions)
    )

  /**
   * Recherche des livres dans le catalogue en fonction d'une requête.
   * La recherche peut être effectuée sur l'ISBN, le titre, les auteurs, le genre, l'année de publication ou la disponibilité.
   *
   * @param x La chaîne de recherche à effectuer.
   * @return Un `Future` contenant une liste de chaînes de caractères représentant les livres trouvés.
   */
  def search(x: String): Future[List[String]] = Future {
    val query = x.toLowerCase
    books
      .filter { book =>
        book.ISBN.value.toLowerCase == query ||
          book.title.toLowerCase.contains(query) ||
          book.authors.exists(_.toLowerCase.contains(query)) ||
          book.genre.toLowerCase.contains(query) ||
          book.publicationyear.toString == query ||
          book.availability.toString == query
      }
      .map(book =>
        s"Book found: ${book.title} by ${book.authors.mkString(", ")}"
      )
      .toList
    }

  /**
   * Retourne des statistiques sur le catalogue, telles que le nombre total de livres,
   * le nombre de livres disponibles et non disponibles, les genres disponibles,
   * l'année moyenne de publication et le genre le plus courant.
   *
   * @return Une chaîne de caractères représentant les statistiques du catalogue.
   */
  def statistics(): String =
    val book_amount = books.size
    val books_available = books.count(_.availability)
    val books_unavailable = book_amount - books_available
    val genres = books.map(_.genre).distinct
    val avgYear: Double = books.map(_.publicationyear).sum.toDouble / books.size
    val genreStats = books.groupBy(_.genre).view.mapValues(_.size).toList
    val mostCommonGenre = genreStats.maxBy(_._2)._1
    val user_names = users.map(_.name).mkString(", ")

    s"""
            Total books: ${book_amount}
            Available books: ${books_available}
            Unavailable books: ${books_unavailable}
            Genres available: ${genres.mkString(", ")}
            Average publication year: ${avgYear.formatted("%.2f")}
            Most common genre: $mostCommonGenre
          """

  /**
   * Recommande des livres pour un utilisateur en fonction de son historique d'emprunts.
   * Cette méthode recommande des livres qui ne sont pas encore empruntés par l'utilisateur,
   * mais qui partagent des genres ou des auteurs avec les livres qu'il a empruntés.
   *
   * @param user L'utilisateur pour lequel les recommandations sont faites.
   * @return Un `Future` contenant une liste de livres recommandés.
   */
  def recommendBooksFor(user: User): Future[List[Book_Entity]] = Future {
    // Liste des livres empruntés par l'utilisateur
    val borrowedBooks = transactions.filter(_.user.id == user.id).map(_.book_loans)

    // Genres et auteurs préférés de l'utilisateur
    val preferredGenres = borrowedBooks.map(_.genre).toSet
    val preferredAuthors = borrowedBooks.flatMap(_.authors).toSet

    // ISBN des livres empruntés
    val borrowedISBNs = borrowedBooks.map(_.ISBN).toSet

    // Filtrage des livres recommandés : ils doivent être disponibles, non empruntés, et correspondant aux genres, ou auteurs préférés
    books.filter { book =>
      book.availability &&
        !borrowedISBNs.contains(book.ISBN) &&
        (preferredGenres.contains(book.genre) ||
          book.authors.exists(preferredAuthors.contains))
    }.toList
  }
