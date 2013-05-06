package org.chris_martin.georgia

class PaymentTest extends org.scalatest.FreeSpec {

  implicit def parseBigDecimal(s: String): BigDecimal = BigDecimal(s)

  val payments = Seq(
    Payment("A,DISI", "STUDENT ASSISTANT", "5183.33", "0.00"),
    Payment("AABED,MOHAMMED A", "GRADUATE RESEARCH ASSISTANT", "24810.69", "800.58"),
    Payment("AALFS,DAVID D", "RESEARCH PROFESSIONAL AD", "151962.08", "31586.76"),
    Payment("AARON,DEKISHA M", "SERVICE / MAINTENANCE WORKER", "27637.78", "0.00"),
    Payment("ABAYOMI,KOBI A", "ASSISTANT PROFESSOR", "87053.22", "11269.64"),
    Payment("ABBASPOUR,REZA", "GRADUATE RESEARCH ASSISTANT", "11880.00", "0.00"),
    Payment("ABBOTT,JAMES E", "SERVICE / MAINTENANCE WORKER", "29602.24", "0.00"),
    Payment("ABBOTT,PHILLIP L", "SERVICE / MAINTENANCE WORKER", "33315.94", "0.00"),
    Payment("ABBOTT-LYON,HEATHER L", "FELLOW AD", "-124.56", "0.00"),
    Payment("ABBOUD,JOSEF B", "GRADUATE RESEARCH ASSISTANT", "8230.00", "0.00"),
    Payment("ABDALLAH,JASSEM A", "FELLOW AD", "10103.19", "0.00"),
    Payment("ABDEL-KHALIK,SAID I", "PROFESSOR", "152890.11", "5015.86"),
    Payment("ABDEL-WAHED,MAHMOUD A", "RESEARCH PROFESSIONAL AD", "46287.00", "0.00"),
    Payment("ABDELAAL,MAHMOUD A", "GRADUATE RESEARCH ASSISTANT", "24050.00", "0.00"),
    Payment("ABDELNOUR,ALEX G", "GRADUATE ASSISTANT", "5100.00", "0.00"),
    Payment("ABDUR-RAHIM,AMIR S", "COMMUNICATIONS PROFESSIONAL", "67378.30", "0.00"),
    Payment("ABDUR-RAHMAN,ABDUL-KHAALIQ S", "SECURITY GUARD", "36563.74", "0.00"),
    Payment("ABEDIN,YOUSUF", "STUDENT ASSISTANT", "940.00", "0.00"),
    Payment("ABELEW,THOMAS A", "RESEARCH PROFESSIONAL AD", "32400.00", "0.00"),
    Payment("ABERLE-GRASSE,MELISSA J", "LECTURER", "39883.07", "0.00"),
    Payment("ABERNATHY,ROBERT S", "RESEARCH PROFESSIONAL AD", "95328.72", "1959.16"),
    Payment("ABESSI,OZEAIR", "RESEARCH PROFESSIONAL AD", "16176.36", "0.00"),
    Payment("ABLE,EDWARD A", "BUSINESS OPERATIONS PROFESSIONAL", "54000.00", "1395.24"),
    Payment("ABLER,RANDAL T", "RESEARCH PROFESSIONAL AD", "115710.72", "4963.42")
  )

  "Parse payments" in {
    expectResult(payments) {
      Payment.parseCsv(getClass.getClassLoader.getResource("Payment-listing.csv")).take(payments.size).toList
    }
  }

}
