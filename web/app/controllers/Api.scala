package controllers

import play.api._, mvc._, db._, Play.current

import org.jooq.{Record, RecordMapper, SQLDialect, DSLContext}
import org.jooq.impl.DSL
import org.jooq.conf.Settings

import scala.util.parsing.json.{JSONArray, JSONObject}
import scala.collection.JavaConversions._

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

  def orgtypes = Action {
    Ok {
      JSONObject(Map(
        "orgtypes" -> JSONArray(
          (create
            selectFrom(Tables.ORG_TYPES)
            fetch new RecordMapper[Record, JSONObject] {

              def map(record: Record) =
                JSONObject(Map(
                  "id" -> record.getValue(Tables.ORG_TYPES.ID),
                  "title" -> record.getValue(Tables.ORG_TYPES.TITLE)
                ))

            }
          ).toList
        )
      )).toString()
    }
  }

}
