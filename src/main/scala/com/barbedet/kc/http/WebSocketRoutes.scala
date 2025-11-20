package com.barbedet.kc.http

import cats.effect.IO
import fs2.{Pipe, Stream}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame

import scala.concurrent.duration._

object WebSocketRoutes {

  /**
   * Expose un endpoint WebSocket sur :
   *   GET /ws/echo
   */
  def routes(builder: WebSocketBuilder2[IO]): HttpRoutes[IO] = {
    object dsl extends Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {

      case GET -> Root / "echo" =>
        // Stream vide - ne pas envoyer de messages automatiques
        val send: Stream[IO, WebSocketFrame] =
          Stream.empty

        // Ce qu'on fait des messages reçus du client
        val receive: Pipe[IO, WebSocketFrame, Unit] =
          _.evalMap {
            case WebSocketFrame.Text(text, _) =>
              IO.println(s"[WS] Reçu du client: $text")

            case other =>
              IO.println(s"[WS] Frame non gérée: $other")
          }

        builder.build(send, receive)
    }
  }
}
