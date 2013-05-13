import sbt._
import Keys._

object Build extends sbt.Build {

  lazy val project = Project("build-build", file(".")).settings(
    libraryDependencies ++= Seq(
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "org.jooq" % "jooq-codegen" % "3.0.0"
    )
  )

}
