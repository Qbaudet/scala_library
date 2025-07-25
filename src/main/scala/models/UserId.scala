package models

import upickle.default.{ReadWriter, readwriter}

/**
 * Représente un identifiant unique pour un utilisateur.
 * Ce type utilise un **type opaque** pour encapsuler un entier (`Int`) et garantir que l'ID
 * est traité comme un type distinct des entiers classiques.
 *
 * @param id L'identifiant de l'utilisateur sous forme d'entier.
 */
object UserId:
  opaque type UserId = Int

  /**
   * Crée un `UserId` à partir d'un entier.
   * Cette méthode permet de créer un identifiant d'utilisateur tout en l'encapsulant dans le type opaque `UserId`.
   *
   * @param id L'entier représentant l'identifiant de l'utilisateur.
   * @return Un `UserId` encapsulant l'entier fourni.
   */
  def apply(id: Int): UserId = id

  /**
   * Extension de la classe `UserId` pour ajouter des méthodes supplémentaires permettant de manipuler l'ID.
   */
  extension (uid: UserId)

    /**
     * Retourne la valeur de l'ID utilisateur sous forme d'entier.
     *
     * @return L'entier représentant l'identifiant de l'utilisateur.
     */
    def value: Int = uid

  /**
   * Sérialisation et désérialisation pour le type opaque `UserId` avec `uPickle`.
   * Utilise la méthode `bimap` pour convertir un `UserId` en entier et inversement.
   *
   * Cette conversion permet de sérialiser un `UserId` en entier et de le désérialiser à partir d'un entier.
   */
  given rwUserId: ReadWriter[UserId] =
  readwriter[Int].bimap(UserId(_), _.value)

  /**
   * Instance de `Ordering` pour `UserId`.
   * Permet de comparer des `UserId` en utilisant leur valeur interne (`Int`).
   * Ceci est utile pour trier les utilisateurs par leur identifiant.
   */
  given Ordering[UserId] = (x: UserId, y: UserId) => x.value compare y.value
