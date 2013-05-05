package org.chris_martin.georgia

import scalaj.http.Http.Request
import scalaj.http.Http

package object http {

  import Implicits._

  val cookie = """DoaaDisclaimerAccepted="DOAA DISCLAIMER READ AND ACCEPTED.""""

  def urlTransform(url: String): String =
    if (url.startsWith("/")) "http://open.georgia.gov%s".format(url) else url

  def get(url: String): Request =
    Http(urlTransform(url)).cookie(cookie)

  def post(url: String, data: String): Request =
    Http.postData(urlTransform(url), data).cookie(cookie)

  object Implicits {

    implicit class RichRequest(request: Request) {

      def cookie(cookie: String): Request =
        request.header("Cookie", cookie)

    }

  }

}
