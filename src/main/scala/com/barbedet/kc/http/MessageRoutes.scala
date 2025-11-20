package com.barbedet.kc.http

import cats.effect.IO
import cats.effect.kernel.Ref
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import com.barbedet.kc.model.{CreateMessage, Message}
import com.barbedet.kc.model.CreateMessage._
import com.barbedet.kc.model.Message._

/**
 * Routes REST :
 *   - GET  /api/messages
 *   - GET  /api/messages/:id
 *   - POST /api/messages
 *
 * Stockage en mémoire via Ref[IO, Map[Long, Message]].
 */
class MessageRoutes(
    messagesRef: Ref[IO, Map[Long, Message]],
    idRef: Ref[IO, Long]
) extends Http4sDsl[IO] {

  // Décodage JSON -> CreateMessage
  implicit private val createMessageEntityDecoder =
    jsonOf[IO, CreateMessage]

  // Encodage Message -> JSON
  implicit private val messageEntityEncoder =
    jsonEncoderOf[IO, Message]

  // Encodage List[Message] -> JSON
  implicit private val messagesEntityEncoder =
    jsonEncoderOf[IO, List[Message]]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    // GET /api/messages
    case GET -> Root / "messages" =>
      for {
        messages <- messagesRef.get
        resp     <- Ok(messages.values.toList.asJson)
      } yield resp

    // GET /api/messages/:id
    case GET -> Root / "messages" / LongVar(id) =>
      for {
        messages <- messagesRef.get
        resp <- messages.get(id) match {
          case Some(message) =>
            Ok(message.asJson)

          case None =>
            NotFound(
              Map("error" -> s"Message $id not found").asJson
            )
        }
      } yield resp

    // POST /api/messages
    //
    // Body JSON attendu :
    // { "content": "mon contenu" }
    case req @ POST -> Root / "messages" =>
      for {
        input <- req.as[CreateMessage]
        id    <- idRef.updateAndGet(_ + 1L)
        msg    = Message(id, input.content)
        _     <- messagesRef.update(_ + (id -> msg))
        resp  <- Created(msg.asJson)
      } yield resp
  }
}
