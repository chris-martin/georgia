package org.chris_martin.georgia

class PaymentTest extends org.scalatest.FreeSpec {

  implicit def parseBigDecimal(s: String): BigDecimal = BigDecimal(s)

  val payments = Seq(
    Payment("BOSS,PATRICK", "TEMPORARY",	"685.00", "140.00"),
    Payment("DARDEN,KAYLEY", "TEMPORARY",	"770.00", "224.00"),
    Payment("KARSTEDT,ETHAN", "TEMPORARY",	"415.00", "112.00"),
    Payment("KARSTEDT,NICOLE", "INSTRUCTOR",	"32504.58", "9019.34"),
    Payment("NEWBERRY,FARRAH", "EXECUTIVE DIRECTOR",	"50074.11", "0.00"),
    Payment("RICE,JACKLEN", "OFFICE ADMINISTRATOR",	"2412.00", "214.50"),
    Payment("TRUELOVE,JERRY", "BOARD MEMBER",	"75.00", "82.50"),
    Payment("WHITED,BOBBIE", "OFFICE ADMINISTRATOR",	"1042.52",	"0.00")
  )

  "Parse payments" in {
    expectResult(payments) {
      Payment.fetchAll("2010", "1", "934")
    }
  }

}
