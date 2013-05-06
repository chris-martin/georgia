package org.chris_martin.georgia

import au.com.bytecode.opencsv.CSVParser
import java.net.URL
import http.Implicits._

case class Payment(personName: String, jobTitle: String, salary: BigDecimal, travel: BigDecimal)

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

}
