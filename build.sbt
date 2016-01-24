name := "xkcd"

version := "1.0"

scalaVersion := "2.11.7"

organization := "org.remus32.xkcd"

libraryDependencies += "com.google.code.gson" % "gson" % "2.5"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"
libraryDependencies += "commons-cli" % "commons-cli" % "1.3.1"
libraryDependencies += "jline" % "jline" % "2.13"
//libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"

scalacOptions += "-target:jvm-1.8"