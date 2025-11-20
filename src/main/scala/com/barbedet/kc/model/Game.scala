package com.barbedet.kc.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

/** État d'une lettre dans la grille */
sealed trait LetterState
object LetterState {
  case object Correct extends LetterState   // Lettre bien placée (vert)
  case object Present extends LetterState   // Lettre présente mais mal placée (jaune)
  case object Absent extends LetterState    // Lettre absente (gris)
  case object Unknown extends LetterState   // Pas encore jouée

  implicit val encoder: Encoder[LetterState] = Encoder.encodeString.contramap {
    case Correct => "correct"
    case Present => "present"
    case Absent  => "absent"
    case Unknown => "unknown"
  }

  implicit val decoder: Decoder[LetterState] = Decoder.decodeString.map {
    case "correct" => Correct
    case "present" => Present
    case "absent"  => Absent
    case _         => Unknown
  }
}

/** Une lettre avec son état */
case class Letter(char: Char, state: LetterState)

object Letter {
  implicit val encoder: Encoder[Letter] = deriveEncoder
  implicit val decoder: Decoder[Letter] = deriveDecoder
}

/** Un essai (tentative de mot) */
case class Attempt(word: String, letters: List[Letter])

object Attempt {
  implicit val encoder: Encoder[Attempt] = deriveEncoder
  implicit val decoder: Decoder[Attempt] = deriveDecoder
}

/** État d'une partie de Tusmo */
case class GameState(
  gameId: String,
  wordToGuess: String,        // Le mot à deviner (caché au client)
  attempts: List[Attempt],     // Les tentatives
  maxAttempts: Int,
  isWon: Boolean,
  isLost: Boolean,
  firstLetter: Char            // Première lettre (indice)
)

object GameState {
  // Version publique sans révéler le mot (sauf si perdu)
  case class PublicGameState(
    gameId: String,
    attempts: List[Attempt],
    maxAttempts: Int,
    isWon: Boolean,
    isLost: Boolean,
    firstLetter: Char,
    wordLength: Int,
    revealedWord: Option[String],  // Le mot révélé si partie perdue
    debugWord: Option[String]       // Le mot à deviner (mode debug uniquement)
  )

  implicit val publicEncoder: Encoder[PublicGameState] = deriveEncoder
  implicit val publicDecoder: Decoder[PublicGameState] = deriveDecoder

  def toPublic(state: GameState, debugMode: Boolean = false): PublicGameState = PublicGameState(
    gameId = state.gameId,
    attempts = state.attempts,
    maxAttempts = state.maxAttempts,
    isWon = state.isWon,
    isLost = state.isLost,
    firstLetter = state.firstLetter,
    wordLength = state.wordToGuess.length,
    revealedWord = if (state.isLost) Some(state.wordToGuess) else None,
    debugWord = if (debugMode) Some(state.wordToGuess) else None  // Mot visible en mode debug
  )
}

/** Requête pour soumettre un mot */
case class GuessRequest(word: String)

object GuessRequest {
  implicit val decoder: Decoder[GuessRequest] = deriveDecoder
}

/** Statistiques du joueur */
case class PlayerStats(
  currentStreak: Int,    // Série actuelle de victoires consécutives
  bestStreak: Int        // Meilleur score de série
)

object PlayerStats {
  implicit val encoder: Encoder[PlayerStats] = deriveEncoder
  implicit val decoder: Decoder[PlayerStats] = deriveDecoder
  
  // Stats initiales
  def empty: PlayerStats = PlayerStats(currentStreak = 0, bestStreak = 0)
  
  // Mettre à jour après une victoire
  def recordWin(stats: PlayerStats): PlayerStats = {
    val newStreak = stats.currentStreak + 1
    PlayerStats(
      currentStreak = newStreak,
      bestStreak = math.max(newStreak, stats.bestStreak)
    )
  }
  
  // Réinitialiser après une défaite
  def recordLoss(stats: PlayerStats): PlayerStats = {
    stats.copy(currentStreak = 0)
  }
}
