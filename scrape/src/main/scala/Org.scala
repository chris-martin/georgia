package org.chris_martin.georgia

import org.mozilla.javascript.{NativeObject, NativeArray, ScriptableObject, Context}
import java.io.Reader
import collection.JavaConversions._
import util.parsing.json.{JSONArray, JSONObject}

case class Org(id: String, title: String) {

  def json: JSONObject =
    JSONObject(Map(
      "id" -> id,
      "title" -> title
    ))

}

object Org {

  def fetchAll(year: String, orgTypeId: String): Seq[Org] =
    parseJs(fetchJs(year, orgTypeId))

  def fetchJs(year: String, orgTypeId: String): String =
    http.post(
      url = "/sta/dwr/call/plaincall/stDWRService.getEntitiesByOrgTypeYear.dwr",
      data = Map(
        "callCount" -> "1",
        "scriptSessionId" -> "",
        "c0-scriptName" -> "stDWRService",
        "c0-methodName" -> "getEntitiesByOrgTypeYear",
        "c0-id" -> "0",
        "c0-param0" -> "string:%s".format(orgTypeId),
        "c0-param1" -> "string:%s".format(year),
        "batchId" -> "0"
      ).map(x => x._1 + "=" + x._2).mkString("\n")
    ).asString

  def parseJs(js: String): Seq[Org] = parseJs(
    (scope: ScriptableObject, context: Context) => {
      context.evaluateString(scope, js.dropWhile(_ != '\n'), "ajaxResponse.js", 1, null)
    }
  )

  def parseJs(js: Reader): Seq[Org] = {
    Stream.continually(js.read()).dropWhile(_ != '\n')
    parseJs(
      (scope: ScriptableObject, context: Context) => {
        context.evaluateReader(scope, js, "ajaxResponse.js", 1, null)
      }
    )
  }

  def parseJs(js: (ScriptableObject, Context) => Unit): Seq[Org] = {
    val context = Context.enter()
    val scope = context.initStandardObjects()
    context.evaluateString(scope,
      """
        |var result;
        |
        |dwr = {};
        |dwr.engine = {};
        |dwr.engine._remoteHandleCallback = function (a, b, c) {
        |  result = c;
        |}
      """.stripMargin, "setup.js", 1, null)
    js(scope, context)
    scope.get("result").asInstanceOf[NativeArray].map(_.asInstanceOf[NativeObject]) map { obj =>
      Org(
        id = obj.get("id").toString,
        title = obj.get("name").toString
      )
    }
  }

  def json(orgs: Seq[Org]): JSONObject =
    JSONObject(Map(
      "orgs" -> JSONArray(
        orgs.sortBy(_.id).map(_.json).toList
      )
    ))

  def parse(json: JSONObject): Org =
    Org(
      id = json.obj("id").asInstanceOf[String],
      title = json.obj("title").asInstanceOf[String]
    )

  def parseMany(json: JSONObject): Seq[Org] =
    json.obj("orgs").asInstanceOf[JSONArray].list.map({ x =>
      parse(x.asInstanceOf[JSONObject])
    })

}
