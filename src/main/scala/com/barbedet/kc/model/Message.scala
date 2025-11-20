package com.barbedet.kc.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

/** Ressource principale de l'API : un message simple. */
final case class Message(
  id: Long,
  content: String
)

object Message {
  implicit val messageEncoder: Encoder[Message] = deriveEncoder[Message]
  implicit val messageDecoder: Decoder[Message] = deriveDecoder[Message]
}

/** Payload utilisé pour la création d'un message (POST /messages). */
final case class CreateMessage(
  content: String
)

object CreateMessage {
  implicit val createMessageDecoder: Decoder[CreateMessage] =
    deriveDecoder[CreateMessage]
}
