import sbt.Keys._

lazy val commonSettings = Seq(
  organization := "org.remus32",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.7",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test",
  libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.14",
  scalacOptions += "-target:jvm-1.8",
  coverageEnabled := true
)

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "xkcdApp",
    libraryDependencies += "jline" % "jline" % "2.13",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"
  ).
  settings(packAutoSettings).
  settings(
    packMain := Map("xkcd" -> "org.remus32.xkcd.Main"),
    packJvmOpts := Map("xkcd" -> Seq("-Xmx512m")),
    packGenerateWindowsBatFile := false,
    packJarNameConvention := "no-version"
  ).
  dependsOn(lib)

lazy val lib = (project in file("xkcdLib")).
  settings(commonSettings: _*).
  settings(
    name := "xkcdLib",
    libraryDependencies += "com.google.code.gson" % "gson" % "2.5",
    libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2",
    libraryDependencies += "org.rauschig" % "jarchivelib" % "0.7.1",
    libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.1"
  )
/*
libraryDependencies += "com.google.code.gson" % "gson" % "2.5"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"
libraryDependencies += "commons-cli" % "commons-cli" % "1.3.1"
libraryDependencies += "jline" % "jline" % "2.13"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"
libraryDependencies += "org.rauschig" % "jarchivelib" % "0.7.1"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
libraryDependencies += "org.slf4j" % "slf4j-jdk14" % "1.7.14"
*/


