import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "kprifogle",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-Ypartial-unification"
      )
    )
  ),
  name := "cats-effect-demo",
  libraryDependencies ++= Seq(
    catsEffect,
    scio,
    monixEval,
    fs2,
    sparkCore,
    sparkSQL,
    http4sServer,
    http4sSClient,
    http4sDsl,
    http4sSCirce,
    circeCore,
    circeGeneric,
    doobieCore,
    doobieH2,
    sttp,
    sttpCats,
    simulcrum,
    scalaTest,
    frameless,
    framelessCats,
    framelessML,
  )
)