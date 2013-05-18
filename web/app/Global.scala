import com.typesafe.config.ConfigFactory
import play.api._
import scala.collection.JavaConversions._

class Global extends GlobalSettings {

  override def configuration =
    super.configuration ++
    Configuration(
      ConfigFactory.parseMap(Map("db.pass" -> databasePassword))
    )

  private lazy val databasePassword: String = {
    val database = "georgia"
    val user = "georgia"
    val pattern = new scala.util.matching.Regex(
      """([^:]+):(\d+)+:([^:]+):([^:]+):(.*)""",
      "host", "port", "database", "user", "pass"
    )
    val file = new java.io.File(scala.sys.env("HOME"), ".pgpass")
    scala.io.Source.fromFile(file).getLines().map({ line =>
      pattern.findFirstMatchIn(line) match {
        case Some(m)
          if m.group("host") == "localhost"
            && m.group("port") == "5432"
            && m.group("database") == database
            && m.group("user") == user
        => Some(m.group("pass"))
        case _ => None
      }
    }).filter(_.isDefined).next().get
  }

}
