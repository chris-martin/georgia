package org.chris_martin.georgia

import xml.{XML, Elem, Text}
import java.net.URL
import util.parsing.json._

case class OrgType(id: String, title: String)

object OrgType {

  def fetchAll: Seq[OrgType] = parseHtml(fetchHtml)

  def fetchHtml: String =
    http.post(
      url = "/sta/entryPoint.aud",
      data = Map("selectedEntryPoint" -> "OrgEntity").map(x => x._1 + "=" + x._2).mkString("&")
    ).asString

  def xml = XML.withSAXParser(
    new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser()
  )

  def parseHtml(html: String): Seq[OrgType] =
    parseHtml(xml.loadString(html))

  def parseHtml(html: URL): Seq[OrgType] =
    parseHtml(xml.load(html))

  def parseHtml(html: Elem): Seq[OrgType] = (
    html \\ "div"
    filter {
      _ \ "input" exists {
        _ \ "@name" contains Text("searchCriteria.organizationType")
      }
    }
    map { div =>
      OrgType(
        id = (div \ "input" \ "@value").text,
        title = div.text.trim
      )
    }
  )

  def json(orgTypes: Seq[OrgType]): JSONObject = JSONObject(Map(
    "orgtypes" -> JSONArray(
      orgTypes.sortBy(_.id).map(x => JSONObject(Map(
        "id" -> x.id,
        "title" -> x.title
      ))).toList
    )
  ))

}
