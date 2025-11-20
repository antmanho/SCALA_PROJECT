ThisBuild / scalaVersion := "2.13.12"

lazy val http4sVersion = "0.23.23"
lazy val circeVersion  = "0.14.6"

lazy val root = (project in file("."))
  .settings(
    name := "scala-http-kc",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl"          % http4sVersion,
      "org.http4s" %% "http4s-circe"        % http4sVersion,
      "io.circe"   %% "circe-generic"       % circeVersion,
      "io.circe"   %% "circe-parser"        % circeVersion,
      "org.http4s" %% "http4s-server"       % http4sVersion
    )
  )
