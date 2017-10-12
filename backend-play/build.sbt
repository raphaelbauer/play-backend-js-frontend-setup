// NOTE
// RESIST THE TEMPTATION TO DO A LOT OF STUFF IN THIS FILE.
// SBT IS COOL. BUT IT CAN BECOME A DEAD-COMPLICATED AND UN-MAINTAINABLE MESS
// IF YOU BLOAT THE BUILD.SBT.
//
// IF YOU ADD EVEN A SINGLE LINE: ASK YOURSELF: IS THERE A DIFFERENT WAY
// TO ACHIEVE THIS?
name := """myapp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += ehcache
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.3"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test

// Show full stacktraces when tests fail. Makes debugging more easy...
testOptions in Test += Tests.Argument("-oF")