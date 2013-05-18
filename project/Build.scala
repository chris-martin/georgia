import sbt._
import sbt.Keys._

object Build extends sbt.Build {

  lazy val root = Project("root", file("."))
    .settings ( globalSettings: _* )
    .aggregate ( scrape, database )

  lazy val scrape = Project("scrape", file("scrape"))
    .configs ( IntegrationTest )
    .settings ( globalSettings ++ Defaults.itSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.scalaj" % "scalaj-http_2.10" % "0.3.7",
        "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
        "org.mozilla" % "rhino" % "1.7R4",
        "net.sf.opencsv" % "opencsv" % "2.3",
        scalatest % "it,test"
      )
    ) : _*)

  lazy val jooqCodegenTask = TaskKey[Unit]("jooq-codegen")
  lazy val jooqCodegenConfig = SettingKey[File => xml.Node]("jooq-codegen-config")

  lazy val jsonToSql = Project("json-to-sql", file("json-to-sql"))
    .settings ( globalSettings : _* )
    .dependsOn ( database, scrape )

  lazy val database = Project("database", file("database"))
    .settings ( globalSettings : _* )
    .settings (
      javaSource in Compile <<= (sourceDirectory in Compile)(_/"jooq"),
      libraryDependencies ++= Seq(
        "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
        "org.jooq" % "jooq" % "3.0.0"
      ),
      jooqCodegenTask <<= (streams, baseDirectory, jooqCodegenConfig) map {
        (s, bd, jc) => {
          import org.jooq.util.{GenerationTool=>G}
          val xml = jc(bd).buildString(stripComments = false)
          s.log.info(xml)
          IO.delete(bd/"src"/"main"/"jooq")
          G.main(G.load(new java.io.ByteArrayInputStream(xml.getBytes("utf-8"))))
        }
      },
      jooqCodegenConfig := { bd: File =>
        val database = "georgia"
        val user = "georgia"
        <configuration>
          <jdbc>
            <driver>org.postgresql.Driver</driver>
            <url>jdbc:postgresql:{ database }</url>
            <user>{ user }</user>
            <password>{
              val pattern = new scala.util.matching.Regex(
                """([^:]+):(\d+)+:([^:]+):([^:]+):(.*)""",
                "host", "port", "database", "user", "pass"
              )
              val file = new java.io.File(scala.sys.env("HOME"), ".pgpass")
              scala.io.Source.fromFile(file).getLines.map({ line =>
                pattern.findFirstMatchIn(line) match {
                  case Some(m) 
                    if m.group("host") == "localhost" 
                    && m.group("port") == "5432"
                    && m.group("database") == database 
                    && m.group("user") == user
                    => Some(m.group("pass"))
                  case _ => None
                }
              }).filter(_.isDefined).next.get
            }</password>
          </jdbc>
          <generator>
            <database>
              <name>org.jooq.util.postgres.PostgresDatabase</name>
              <includes>georgia\.(.*)</includes>
              <excludes></excludes>
            </database>
            <target>
              <packageName>org.chris_martin.georgia.jooq</packageName>
              <directory>{(bd/"src"/"main"/"jooq").toString}</directory>
            </target>
          </generator>
        </configuration>
      }
    )

  lazy val web = {
    play.Project(
      "web",
      "1.0-SNAPSHOT", 
      {
        import play.Project._
        Seq(jdbc, anorm)
      },
      path=file("web")
    )
      .settings( globalSettings : _* )
      .dependsOn ( database )
  }

  type Settings = Seq[Setting[_]]

  lazy val globalSettings: Settings = Seq(
    scalaVersion := "2.10.0"
  )

  lazy val scalatest = "org.scalatest" %% "scalatest" % "1.9.1"

}
