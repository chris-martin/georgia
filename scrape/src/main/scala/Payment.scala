package org.chris_martin.georgia

import au.com.bytecode.opencsv.CSVParser
import java.net.URL
import http.Implicits._
import util.parsing.json.{JSONArray, JSONObject}

case class Payment(personName: String, jobTitle: String, salary: BigDecimal, travel: BigDecimal) {

  def json: JSONObject =
    JSONObject(Map(
      "person_name" -> personName,
      "job_title" -> jobTitle,
      "salary" -> salary.toString,
      "travel" -> travel.toString
    ))

}

object Payment {

  def fetchAll(year: String, orgTypeId: String, orgId: String): Seq[Payment] =
    parseCsv(fetchCsv(year, orgTypeId, orgId))

  def fetchCsv(year: String, orgTypeId: String, orgId: String): String = {

    val jsessionid = http.post(
      url = "/sta/search.aud",
      data = http.encode(Map(
        "searchCriteria.fiscalYear" -> year,
        "searchCriteria.organizationType" -> orgTypeId,
        "searchCriteria.entityId" -> orgId,
        "method:search" -> "Search"
      ))
    ).asCodeHeaders._2.cookie("JSESSIONID").get

    http.get("/sta/reports/salaryTravel.stRpt?rptType=csv")
      .cookie("JSESSIONID=%s".format(jsessionid))
      .asString

  }

  def parseCsv(csv: String): Seq[Payment] =
    parseCsv(csv.split('\n').iterator)

  def parseCsv(csv: URL): Seq[Payment] =
    parseCsv(io.Source.fromURL(csv).getLines())

  def parseCsv(lines: Iterator[String]): Seq[Payment] =
    lines.grouped(21).map(_ drop 1).flatten.map({ line =>
      val cols = new CSVParser().parseLine(line)
      Payment(
        personName = cols(0).trim,
        jobTitle = cols(1).trim,
        salary = parseMoney(cols(2)),
        travel = parseMoney(cols(3))
      )
    }).toSeq

  def parseMoney(s: String): BigDecimal =
    BigDecimal(s.replaceAll("""[\s\$\,]""", ""))

  def json(orgTypes: Seq[Payment]): JSONObject =
    JSONObject(Map(
      "payments" -> JSONArray(
        orgTypes.sortBy(_.personName).map(_.json).toList
      )
    ))

  def parse(json: JSONObject): Payment =
    Payment(
      personName = json.obj("person_name").asInstanceOf[String],
      jobTitle = json.obj("job_title").asInstanceOf[String],
      salary = BigDecimal(json.obj("salary").asInstanceOf[String]),
      travel = BigDecimal(json.obj("travel").asInstanceOf[String])
    )

  def parseMany(json: JSONObject): Seq[Payment] =
    json.obj("payments").asInstanceOf[JSONArray].list.map({ x =>
      parse(x.asInstanceOf[JSONObject])
    })

}
