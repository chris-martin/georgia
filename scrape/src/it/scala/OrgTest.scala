package org.chris_martin.georgia

class OrgTest extends org.scalatest.FreeSpec {

  val orgs = Seq(
    Org("557", "ABRAHAM BALDWIN AGRICULTURAL COLLEGE"),
    Org("521", "ALBANY STATE UNIVERSITY"),
    Org("524", "ARMSTRONG ATLANTIC STATE UNIVERSITY"),
    Org("561", "ATLANTA METROPOLITAN STATE COLLEGE"),
    Org("527", "AUGUSTA STATE UNIVERSITY")
  )

  "Parse orgs" in {
    expectResult(orgs) {
      Org.fetchAll("2012", "2").take(5)
    }
  }

}
