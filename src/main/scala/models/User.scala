package models

import models.Book_Entity
import UserId.{UserId, given}
import services.Catalog
import utils.Validators

/**
 * Représente un utilisateur dans le système.
 * Un utilisateur peut être un membre, un bibliothécaire, ou un membre du corps enseignant.
 * Cette classe contient des informations de base, telles que le nom de l'utilisateur et son identifiant.
 */
sealed trait User:
  def name: String
  def id: UserId

object User:
  import upickle.default.{ReadWriter, macroRW, readwriter}

  /**
   * Sérialisation et désérialisation pour l'utilisateur.
   * Utilise la bibliothèque `uPickle` pour convertir un utilisateur en JSON et inversement.
   */
  given rwUser: ReadWriter[User] = macroRW

/**
 * Représente un membre dans le système. Un membre peut emprunter et rendre des livres.
 *
 * @param name Le nom du membre.
 * @param memberId L'identifiant unique du membre.
 */
case class Member(name: String, memberId: UserId) extends User:
  def id: UserId = memberId

object Member:
  import upickle.default.{ReadWriter, macroRW}

  /**
   * Sérialisation et désérialisation pour le membre.
   */
  given rwMember: ReadWriter[Member] = macroRW

/**
 * Représente un bibliothécaire dans le système.
 * Un bibliothécaire peut ajouter ou retirer des livres du catalogue.
 *
 * @param name Le nom du bibliothécaire.
 * @param employeeId L'identifiant unique du bibliothécaire.
 */
case class Librarian(name: String, employeeId: UserId) extends User:
  def id: UserId = employeeId

  /**
   * Ajoute un livre au catalogue après validation.
   * Si le livre est valide, il est ajouté au catalogue.
   *
   * @param book Le livre à ajouter au catalogue.
   * @param catalog Le catalogue où ajouter le livre.
   */
  def addBook(book: Book_Entity, catalog: Catalog): Either[String, Catalog] =
    Validators.validateBook(book).flatMap(validBook => catalog.addBook(validBook))

  /**
   * Retire un livre du catalogue à partir de son ISBN.
   *
   * @param ISBN Le code ISBN du livre à retirer.
   * @param catalog Le catalogue dont le livre doit être retiré.
   */
  def removeBook(ISBN: String, catalog: Catalog): Unit =
    catalog.books = catalog.books.filterNot(_.ISBN.value == ISBN)
  
object Librarian:
  import upickle.default.{ReadWriter, macroRW}

  /**
   * Sérialisation et désérialisation pour le bibliothécaire.
   */
  given rwLibrarian: ReadWriter[Librarian] = macroRW

/**
 * Représente un membre du corps enseignant dans le système.
 * Un membre du corps enseignant peut ajouter ou retirer des utilisateurs du catalogue.
 *
 * @param name Le nom du membre du corps enseignant.
 * @param facultyId L'identifiant unique du membre du corps enseignant.
 */
case class Faculty(name: String, facultyId: UserId) extends User:
  def id: UserId = facultyId

  /**
   * Ajoute un utilisateur au catalogue après validation.
   * Si l'utilisateur est valide, il est ajouté au catalogue.
   *
   * @param user L'utilisateur à ajouter au catalogue.
   * @param catalog Le catalogue où ajouter l'utilisateur.
   */
  def addUser(user: User, catalog: Catalog): Either[String, Catalog] =
    Validators.validateUser(user).flatMap(validUser => catalog.addUser(validUser))

  /**
   * Retire un utilisateur du catalogue à partir de son identifiant.
   *
   * @param userId L'identifiant de l'utilisateur à retirer.
   * @param catalog Le catalogue dont l'utilisateur doit être retiré.
   */
  def removeUser(userId: UserId, catalog: Catalog): Unit =
    catalog.users = catalog.users.filterNot(_.id == userId)

object Faculty:
  import upickle.default.{ReadWriter, macroRW}

  /**
   * Sérialisation et désérialisation pour le membre du corps enseignant.
   */
  given rwFaculty: ReadWriter[Faculty] = macroRW
