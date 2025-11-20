package com.barbedet.kc.http

import cats.effect.IO
import org.http4s.{HttpRoutes, Request, Response, StaticFile}
import org.http4s.dsl.Http4sDsl

/**
 * Routes pour servir les fichiers statiques (HTML, CSS, JS)
 */
object StaticRoutes extends Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    // Route pour la page d'accueil
    case request @ GET -> Root =>
      serveResource("tusmo.html", request)

    case request @ GET -> Root / "index.html" =>
      serveResource("tusmo.html", request)
      
    case request @ GET -> Root / "tusmo.html" =>
      serveResource("tusmo.html", request)
  }

  private def serveResource(name: String, request: Request[IO]): IO[Response[IO]] = {
    StaticFile.fromResource(name, Some(request)).getOrElseF(NotFound())
  }
}
