package com.barbedet.kc.http

import cats.effect.IO
import org.http4s.{HttpRoutes, Response}
import org.http4s.server.middleware.CORS
import org.http4s.headers._

/**
 * Middleware CORS pour permettre les requêtes cross-origin
 */
object CorsMiddleware {

  /**
   * Applique le middleware CORS aux routes
   */
  def apply(routes: HttpRoutes[IO]): HttpRoutes[IO] = {
    CORS.policy
      .withAllowOriginAll
      .withAllowMethodsAll
      .withAllowHeadersAll
      .withAllowCredentials(false)
      .apply(routes)
  }
}
