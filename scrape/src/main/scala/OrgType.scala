package org.chris_martin.georgia

import xml.{XML, Elem, Text}
import java.net.URL
import util.parsing.json._

case class OrgType(id: String, title: String) {

  def json: JSONObject =
    JSONObject(Map(
      "id" -> id,
      "title" -> title
    ))

}

object OrgType {

  def fetchAll: Seq[OrgType] = parseHtml(fetchHtml)

  def fetchHtml: String =
    http.post(
      url = "/sta/entryPoint.aud",
      data = http.encode(Map("selectedEntryPoint" -> "OrgEntity"))
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
      orgTypes.sortBy(_.id).map(_.json).toList
    )
  ))

  def parse(json: JSONObject): OrgType =
    OrgType(
      id = json.obj("id").asInstanceOf[String],
      title = json.obj("title").asInstanceOf[String]
    )

  def parseMany(json: JSONObject): Seq[OrgType] =
    json.obj("orgtypes").asInstanceOf[JSONArray].list.map({ x =>
      parse(x.asInstanceOf[JSONObject])
    })

}
