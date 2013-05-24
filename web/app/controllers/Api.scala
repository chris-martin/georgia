package controllers

import play.api._, mvc._, db._, Play.current
import cache.Cache.{getOrElse => cache}

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

  implicit class RichResultQuery[R <: Record](q: ResultQuery[R]) {

    def fetch[A](f: Record => A): Seq[A] =
      (q fetch new RecordMapper[Record, A] {
        def map(record: Record): A = f(record)
      }).toSeq

  }

  def obj(entries: (String, _)*): JSONObject =
    JSONObject(Map(entries: _*))

  def arr(entries: Any*): JSONArray = JSONArray(entries.toList)

  def index = Action {
    Ok(views.html.api())
  }

  def orgtypes = Action {
    cache("api.orgtypes") {
      Ok {
        obj(
          "orgtypes" -> arr(
            (create
              selectFrom Tables.ORG_TYPES
              orderBy Tables.ORG_TYPES.ID
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
  }

  def totals = Action {
    cache("api.totals") {
      Ok {
        obj(
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
              orderBy Tables.PAYMENTS.YEAR
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
        ).toString()
      }
    }
  }

/*

  def totalsForYearByOrgType(year: Int) = Action {
    cache("api.totals.year-%d.byOrgtype".format(year)) {
      Ok {
        obj(
          "totals" -> arr(
            (create
              select(
                
              )
            )
          )
        ).toString()
      }
    }
  }

select totals.year,
       totals.org_type_id,
       org_types.title as org_type_title,
       totals.salary,
       totals.travel
from (
  select payments.year, 
         orgs.org_type_id,
         sum(payments.salary) as salary,
         sum(payments.travel) as travel
  from payments 
  join orgs 
  on payments.org_id = orgs.id
  group by orgs.org_type_id, payments.year
) as totals
join org_types
on totals.org_type_id = org_types.id
order by year, org_type_id;

*/

}
