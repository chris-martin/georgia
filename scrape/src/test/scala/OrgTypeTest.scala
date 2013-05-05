package org.chris_martin.georgia

class OrgTypeTest extends org.scalatest.FreeSpec {

  val orgTypes = Seq(
    OrgType("1", "State Agencies, Boards, Authorities and Commissions"),
    OrgType("2", "Units of the University System and Georgia Military College"),
    OrgType("3", "Regional Educational Service Agencies"),
    OrgType("4", "Technical Colleges"),
    OrgType("5", "Local Boards of Education")
  )

  "Parse org types" in {
    expectResult(orgTypes) {
      OrgType.parseHtml(getClass.getClassLoader.getResource("OrgType-listing.html"))
    }
  }

  "Fetch org types" in {
    expectResult(orgTypes) {
      OrgType.fetchAll
    }
  }

}
