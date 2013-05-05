import sbt._
import sbt.Keys._

object Build extends sbt.Build {

  lazy val root = Project("root", file("."))
    .settings ( globalSettings: _* )
    .dependsOn ( scrape )

  lazy val scrape = Project("scrape", file("scrape"))
    .configs ( IntegrationTest )
    .settings ( globalSettings ++ moduleSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.scalaj" % "scalaj-http_2.10" % "0.3.7",
        "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
        "net.sf.jtidy" % "jtidy" % "r938"
      )
    ) : _*)

  type Settings = Seq[Setting[_]]

  lazy val globalSettings: Settings = Seq(
    scalaVersion := "2.10.0"
  )

  lazy val moduleSettings: Settings = Defaults.itSettings ++ Seq(
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "it,test"
  )

}
