package org.chris_martin.georgia

import org.mozilla.javascript.{NativeObject, NativeArray, ScriptableObject, Context}
import java.io.Reader
import collection.JavaConversions._

case class Org(id: String, title: String)

object Org {

  def fetchAll(orgTypeId: String): Seq[Org] = parseJs(fetchHtml(orgTypeId))

  def fetchHtml(orgTypeId: String): String =
    http.post(
      url = "/sta/dwr/call/plaincall/stDWRService.getEntitiesByOrgTypeYear.dwr",
      data = Map(
        "callCount" -> "1",
        "scriptSessionId" -> "",
        "c0-scriptName" -> "stDWRService",
        "c0-methodName" -> "getEntitiesByOrgTypeYear",
        "c0-id" -> "0",
        "c0-param0" -> "string:%s".format(orgTypeId),
        "c0-param1" -> "string:",
        "batchId" -> "0"
      ).map(x => x._1 + "=" + x._2).mkString("\n")
    ).asString.dropWhile(_ != '\n')

  def parseJs(js: String): Seq[Org] = parseJs(
    (scope: ScriptableObject, context: Context) => {
      context.evaluateString(scope, js, "ajaxResponse.js", 1, null)
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

}
