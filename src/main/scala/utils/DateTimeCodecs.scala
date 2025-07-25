package utils

import upickle.default.{ReadWriter, readwriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Fournit les instances de sérialisation et désérialisation (ReadWriter) pour
 * java.time.LocalDateTime afin qu'il puisse être pris en charge par uPickle.
 *
 * Utilise le format ISO_LOCAL_DATE_TIME pour convertir les dates en chaîne et vice versa.
 */
object DateTimeCodecs:
  private val fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  /**
   * Instance uPickle pour convertir LocalDateTime en chaîne ISO et inversement.
   *
   * - Sérialisation : LocalDateTime -> String avec format ISO_LOCAL_DATE_TIME
   * - Désérialisation : String -> LocalDateTime en analysant le format ISO
   */
  given rwLocalDateTime: ReadWriter[LocalDateTime] =
  readwriter[String].bimap(
    // Transforme: LocalDateTime -> String
    dt => dt.format(fmt),
    // Lit: String -> LocalDateTime
    str => LocalDateTime.parse(str, fmt)
  )
