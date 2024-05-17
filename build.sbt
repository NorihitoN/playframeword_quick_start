name := """myscheduler"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
    jdbc,
    evolutions,
    filters,
    "com.h2database"  %  "h2"                           % "2.2.224",
    "org.scalikejdbc" %% "scalikejdbc"                  % "4.3.0",
    "org.scalikejdbc" %% "scalikejdbc-config"           % "4.3.0",
    "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "3.0.0-scalikejdbc-4.2",
)

dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

Test / fork := true
Test / javaOptions += "-Dconfig.file=conf/application.test.conf"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
