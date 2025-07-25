package utils

import models.{Book_Entity, Transaction, User}
import services.Catalog
import java.time.LocalDateTime

/**
 * Objet contenant les méthodes de validation utilisées dans le système.
 * Les validations sont effectuées sur des livres, des utilisateurs et des transactions.
 *
 * Ce module permet de s'assurer que les données sont valides avant qu'elles ne soient utilisées
 * dans le système, par exemple avant d'ajouter des livres ou des utilisateurs au catalogue,
 * ou avant d'enregistrer une transaction.
 */
object Validators:

  /**
   * Valide un livre avant de l'ajouter au catalogue.
   * Cette méthode vérifie que l'ISBN, le titre, les auteurs, l'année de publication et le genre
   * sont valides et conformes aux règles définies.
   *
   * @param book Le livre à valider.
   * @return `Right(book)` si le livre est valide, sinon un message d'erreur dans un `Left`.
   */
  def validateBook(book: Book_Entity): Either[String, Book_Entity] =
    if book.ISBN.value.trim.isEmpty then Left("ISBN is required")
    else if book.title.trim.isEmpty then Left("Title is required")
    else if book.authors.isEmpty then Left("At least one author is required")
    else if book.publicationyear < 1450 || book.publicationyear > java.time.Year.now.getValue
    then Left("Invalid publication year")
    else if book.genre.trim.isEmpty then Left("Genre is required")
    else Right(book)

  /**
   * Valide un utilisateur avant de l'ajouter au catalogue.
   * Cette méthode vérifie que le nom de l'utilisateur n'est pas vide et que son identifiant
   * est positif.
   *
   * @param user L'utilisateur à valider.
   * @return `Right(user)` si l'utilisateur est valide, sinon un message d'erreur dans un `Left`.
   */
  def validateUser(user: User): Either[String, User] =
    if user.name.trim.isEmpty then Left("User name is required")
    else if user.id.value <= 0 then Left("User ID must be positive")
    else Right(user)

  /**
   * Valide une transaction de prêt ou de réservation d'un livre.
   * Cette méthode vérifie que le livre et l'utilisateur existent dans le catalogue, que le livre
   * est disponible pour l'emprunt, et que les dates de retour et de réservation sont valides.
   *
   * @param book Le livre concerné par la transaction.
   * @param user L'utilisateur effectuant l'emprunt ou la réservation.
   * @param catalog Le catalogue contenant les livres et les utilisateurs.
   * @param loanTime L'heure et la date de l'emprunt.
   * @param returnTime Optionnelle : L'heure et la date de retour du livre.
   * @param reservationTime Optionnelle : L'heure et la date de réservation du livre.
   * @return `Right(Transaction)` si la transaction est valide, sinon un message d'erreur dans un `Left`.
   */
  def validateTransaction(
    book: Book_Entity,
    user: User,
    catalog: Catalog,
    loanTime: LocalDateTime,
    returnTime: Option[LocalDateTime] = None,
    reservationTime: Option[LocalDateTime] = None
    ): Either[String, Transaction] =
    if !catalog.books.exists(_.ISBN == book.ISBN) then
      Left("Book does not exist in catalog")
    else if !catalog.users.exists(_.id == user.id) then
      Left("User not found in catalog")
    else if !book.availability then Left("Book is not available for loan")
    else if returnTime.exists(_.isBefore(loanTime)) then
      Left("Return date cannot be before loan date")
    else if reservationTime.exists(_.isBefore(loanTime)) then
      Left("Reservation date cannot be before loan date")
    else Right(Transaction(book, user, loanTime, returnTime, reservationTime))
