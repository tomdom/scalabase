organization := "com.github.tomdom"

name := """scalabase"""

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.4", "2.11.5")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.8",
  "io.spray" %% "spray-client" % "1.3.1"
)

publishTo := {
  val tomdomMvn = Path.userHome.absolutePath + "/projects/github/tomdom/tomdom-mvn"
  if (isSnapshot.value)
    Some(Resolver.file("file",  new File(tomdomMvn + "/snapshots")))
  else
    Some(Resolver.file("file",  new File(tomdomMvn + "/releases")))
}
