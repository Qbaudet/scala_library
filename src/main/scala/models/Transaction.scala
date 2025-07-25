package models

import models.Book_Entity

import java.time.LocalDateTime

import models.User.given

import utils.DateTimeCodecs.given

/**
 * Représente une transaction de prêt d'un livre dans le catalogue.
 * Une transaction enregistre l'emprunt d'un livre par un utilisateur, ainsi que les informations
 * relatives au temps de l'emprunt, au retour et à la réservation.
 *
 * @param book_loans Le livre emprunté, représenté par un objet `Book_Entity`.
 * @param user L'utilisateur qui a emprunté le livre, représenté par un objet `User`.
 * @param timestamp Le moment où la transaction a été effectuée (date et heure de l'emprunt).
 * @param returns Optionnelle : La date et l'heure où le livre doit être retourné, si disponible.
 * @param reservation Optionnelle : La date et l'heure où le livre a été réservé, si une réservation a été effectuée.
 */
case class Transaction(
  book_loans: Book_Entity,
  user: User,
  timestamp: LocalDateTime,
  returns: Option[LocalDateTime] = None,
  reservation: Option[LocalDateTime] = None
  )

object Transaction:
  import upickle.default.{ReadWriter, macroRW, write}
  /**
   * Sérialisation/désérialisation JSON pour Transaction.
   * */
  given rwTransaction: ReadWriter[Transaction] = macroRW
