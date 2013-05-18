import annotation.tailrec
import java.io.{FileWriter, File}
import org.chris_martin.georgia.jooq.georgia.Tables
import org.chris_martin.georgia.OrgType
import org.jooq.conf.{StatementType, Settings}
import org.jooq.impl.DSL
import org.jooq.{Query, SQLDialect}
import util.parsing.json._
import io.Source

object Main {

  def main(args: Array[String]) {

    val jsonDir = new File(args(0))
    val sqlDir = new File(args(1))

    val out = new FileWriter(new File(sqlDir, "data.sql"))
    def write(q: Query) { out.write(q.getSQL); out.write(";\n") }

    @tailrec
    def file(base: File, path: List[String]): File =
      path match {
        case Nil => base
        case head :: tail => file(new File(base, head), tail)
      }

    def json(path: List[String]): JSONObject =
      JSON.parseRaw(
        Source.fromFile(
          file(jsonDir, path)
        ).getLines().mkString("\n")
      ).get.asInstanceOf[JSONObject]

    val create = DSL using (
      SQLDialect.POSTGRES,
      new Settings()
        .withStatementType(StatementType.STATIC_STATEMENT)
    )

    import create.insertInto

    {
      val $ = Tables.ORG_TYPE; import $._
      val query = insertInto($, ID, TITLE)
      OrgType.parseMany(json(List("orgtypes.json"))).
        foreach { x => query.values(x.id, x.title) }
      write(query)
    }

    out.close()

  }

}
