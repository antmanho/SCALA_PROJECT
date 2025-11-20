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
    revealedWord: Option[String]  // Le mot révélé si partie perdue
  )

  implicit val publicEncoder: Encoder[PublicGameState] = deriveEncoder
  implicit val publicDecoder: Decoder[PublicGameState] = deriveDecoder

  def toPublic(state: GameState): PublicGameState = PublicGameState(
    gameId = state.gameId,
    attempts = state.attempts,
    maxAttempts = state.maxAttempts,
    isWon = state.isWon,
    isLost = state.isLost,
    firstLetter = state.firstLetter,
    wordLength = state.wordToGuess.length,
    revealedWord = if (state.isLost) Some(state.wordToGuess) else None
  )
}

/** Requête pour soumettre un mot */
case class GuessRequest(word: String)

object GuessRequest {
  implicit val decoder: Decoder[GuessRequest] = deriveDecoder
}
