package controllers

import play.api._, mvc._, db._, Play.current
import play.cache.Cached

import org.jooq._
import org.jooq.impl.DSL
import org.jooq.conf.Settings

import scala.collection.JavaConversions._
import scala.util.parsing.json.{JSONArray, JSONObject}

import java.sql.DriverManager

import org.chris_martin.georgia.jooq.georgia.Tables

object Api extends Controller {

  def newConnection = DriverManager.getConnection("")

  val create: DSLContext =
    DSL using (
      DB.getDataSource(),
      SQLDialect.POSTGRES,
      new Settings()
    )

  implicit class RichResultQuery(q: ResultQuery) {

    def fetch[A](f: Record => A): Seq[A] =
      (q fetch new RecordMapper[Record, A] {
        def map(record: Record): A = f(record)
      }).toSeq

  }

  def obj(entries: (String, _)*): JSONObject =
    JSONObject(Map(entries: _*))

  def arr(entries: Any*): JSONArray = JSONArray(entries.toList)

  @Cached("api.orgtypes")
  def orgtypes = Action {
    Ok {
      obj(
        "orgtypes" -> arr(
          (create
            selectFrom Tables.ORG_TYPES
            fetch { record: Record =>
              obj(
                "id" -> (record getValue Tables.ORG_TYPES.ID),
                "title" -> (record getValue Tables.ORG_TYPES.TITLE)
              )
            }
          )
        )
      ).toString()
    }
  }

  @Cached("api.totals")
  def totals = Action {
    Ok {
      JSONObject(Map(
        "totals" -> arr(
          (create
            select(
              Tables.PAYMENTS.YEAR,
              DSL.count(),
              DSL.sum(Tables.PAYMENTS.SALARY) as Tables.PAYMENTS.SALARY.getName,
              DSL.sum(Tables.PAYMENTS.TRAVEL) as Tables.PAYMENTS.TRAVEL.getName
            )
            from Tables.PAYMENTS
            groupBy Tables.PAYMENTS.YEAR
            fetch { record: Record =>
              obj(
                "year" -> (record getValue Tables.PAYMENTS.YEAR),
                "payments" -> (record getValue DSL.count()),
                "salary" -> (record getValue Tables.PAYMENTS.SALARY),
                "travel" -> (record getValue Tables.PAYMENTS.TRAVEL)
              )
            }
          ) : _*
        )
      ))
    }
  }

}
