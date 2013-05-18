import java.io.{Writer, FileWriter, File}
import org.chris_martin.georgia.jooq.georgia.Tables
import org.chris_martin.georgia.{Payment, Org, OrgType}
import org.jooq.conf.{StatementType, Settings}
import org.jooq.impl.DSL
import org.jooq.{Query, SQLDialect}
import util.parsing.json._
import io.Source

object Main {

  implicit class RichFile(file: File) {
    def / (subdir: String): File = new File(file, subdir)
  }

  implicit class RichWriter(writer: Writer) {
    def write(q: Query) {
      writer.write(q.getSQL); writer.write(";\n")
    }
  }

  implicit def bigDecimalToJava(x: BigDecimal): java.math.BigDecimal =
    x.bigDecimal

  def main(args: Array[String]) {

    val jsonDir = new File(args(0))
    val sqlDir = new File(args(1))

    def json(file: File): JSONObject =
      JSON.parseRaw(
        Source.fromFile(file).getLines().mkString("\n")
      ).get.asInstanceOf[JSONObject]

    val create = DSL using (
      SQLDialect.POSTGRES,
      new Settings()
        .withStatementType(StatementType.STATIC_STATEMENT)
      )

    import create.insertInto

    {
      val out = new FileWriter(new File(sqlDir, "orgtypes.sql"))
      out.write {
        val $ = Tables.ORG_TYPES; import $._
        val query = insertInto($, ID, TITLE)
        val orgtypes = OrgType.parseMany(json(jsonDir/"orgtypes.json"))
        orgtypes foreach { orgtype =>
          query.values(orgtype.id, orgtype.title)
        }
        query
      }
      out.close()
    }

    (jsonDir/"years").listFiles().toSeq.par foreach { yearDir =>
      val year = yearDir.getName
      val out = new FileWriter(new File(sqlDir, "%s.sql".format(year)))
      val orgTypeIds = (yearDir/"orgtypes").listFiles().map(_.getName)

      orgTypeIds foreach { orgtypeId =>

        val orgs = Org.parseMany(json(
          jsonDir/"years"/year/"orgtypes"/orgtypeId/"orgs.json"
        ))

        out.write {
          val $ = Tables.ORGS; import $._
          val query = insertInto($, YEAR, ORG_TYPE_ID, ID, TITLE)
          orgs foreach { org =>
            query.values(year, orgtypeId, org.id, org.title)
          }
          query
        }

        orgs foreach { org =>
          out.write {
            val $ = Tables.PAYMENTS; import $._
            val query = insertInto($, ORG_ID, PERSON_NAME, JOB_TITLE, SALARY, TRAVEL)
            val payments = Payment.parseMany(json(
              jsonDir/"years"/year/"orgtypes"/orgtypeId/"orgs"/org.id/"payments.json"
            ))
            payments foreach { pay =>
              query.values(org.id, pay.personName, pay.jobTitle, pay.salary, pay.travel)
            }
            query
          }
        }

      }
      out.close()
    }

  }

}
