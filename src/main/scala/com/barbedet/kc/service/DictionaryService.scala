package com.barbedet.kc.service

import cats.effect.IO
import scala.io.Source

/**
 * Service de validation de mots français via dictionnaire local
 * Utilise la liste Gutenberg de 336k mots français
 */
object DictionaryService {

  // Chargement lazy du dictionnaire français (336k mots)
  private lazy val frenchWords: Set[String] = {
    val source = Source.fromResource("french-words.txt")
    try {
      source.getLines().map(_.toLowerCase.trim).toSet
    } finally {
      source.close()
    }
  }

  /**
   * Vérifie si un mot existe en français via le dictionnaire local Gutenberg
   * Contient 336 531 mots français
   */
  def isValidFrenchWord(word: String): IO[Boolean] = {
    val normalizedWord = word.toLowerCase.trim
    val isValid = frenchWords.contains(normalizedWord)
    IO.println(s"[Dict] Vérification '$word': ${if (isValid) "✓ VALIDE" else "✗ INVALIDE"} (local)").as(isValid)
  }

  /**
   * Retourne le nombre de mots dans le dictionnaire
   */
  def dictionarySize: Int = frenchWords.size
}
