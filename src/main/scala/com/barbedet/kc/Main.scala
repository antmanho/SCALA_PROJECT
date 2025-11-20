package com.barbedet.kc

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Ref
import com.barbedet.kc.http.{MessageRoutes, WebSocketRoutes, StaticRoutes, CorsMiddleware, TusmoRoutes}
import com.barbedet.kc.model.{Message, GameState, PlayerStats}
import com.barbedet.kc.service.DictionaryService
import com.comcast.ip4s._
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.websocket.WebSocketBuilder2

object Main extends IOApp {

  /**
   * Construit l'application HTTP complète (REST + WebSocket + Static Files + Tusmo)
   * à partir du state en mémoire.
   */
  private def httpApp(
      messagesRef: Ref[IO, Map[Long, Message]],
      idRef: Ref[IO, Long],
      gamesRef: Ref[IO, Map[String, GameState]],
      statsRef: Ref[IO, PlayerStats]
  )(wsBuilder: WebSocketBuilder2[IO]): HttpApp[IO] = {

    val apiRoutes = CorsMiddleware(new MessageRoutes(messagesRef, idRef).routes)
    val tusmoRoutes = CorsMiddleware(new TusmoRoutes(gamesRef, statsRef).routes)
    val wsRoutes  = WebSocketRoutes.routes(wsBuilder)
    val staticRoutes = StaticRoutes.routes

    Router(
      "/api/messages" -> apiRoutes,  // => /api/messages/*
      "/api/game" -> tusmoRoutes,    // => /api/game/*
      "/ws"  -> wsRoutes,            // => /ws/echo
      "/"    -> staticRoutes         // => / (page d'accueil)
    ).orNotFound
  }

  override def run(args: List[String]): IO[ExitCode] =
    for {
      // State en mémoire (pas de base de données)
      messagesRef <- Ref.of[IO, Map[Long, Message]](Map.empty)
      idRef       <- Ref.of[IO, Long](0L)
      gamesRef    <- Ref.of[IO, Map[String, GameState]](Map.empty)
      statsRef    <- Ref.of[IO, PlayerStats](PlayerStats.empty)

      // Afficher le nombre de mots dans le dictionnaire au démarrage
      dictSize = DictionaryService.dictionarySize
      _ <- IO.println(s"📚 Dictionnaire français chargé : $dictSize mots (Gutenberg)")

      _ <- EmberServerBuilder
        .default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpWebSocketApp(httpApp(messagesRef, idRef, gamesRef, statsRef))
        .build
        .use { _ =>
          IO.println("✅ Serveur TUSMO démarré sur http://localhost:8080") >>
          IO.println("   🎯 Jeu TUSMO : http://localhost:8080") >>
          IO.println("   📝 API Game  : http://localhost:8080/api/game") >>
          IO.println("   📨 API Msg   : http://localhost:8080/api/messages") >>
          IO.println("   🔥 Stats     : http://localhost:8080/api/stats") >>
          IO.never
        }
    } yield ExitCode.Success
}
