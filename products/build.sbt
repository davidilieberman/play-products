name := """products"""
organization := "com.dil"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.dil.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.dil.binders._"
