package org.chris_martin.georgia.http

class HttpTest extends org.scalatest.FreeSpec {

  "Parse jsessionid cookie header" in {
    expectResult(Some("66063403AE0B93A989EF2D7319BB5CC3")) {
      new Implicits.Headers(Map(
        "Set-Cookie" -> List("JSESSIONID=66063403AE0B93A989EF2D7319BB5CC3; Path=/sta; HttpOnly"
      ))).cookie("JSESSIONID")
    }
  }

}
