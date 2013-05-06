package org.chris_martin.georgia

import scalaj.http.Http.Request
import scalaj.http.{HttpOptions, Http}
import java.net.URLEncoder

package object http {

  import Implicits._

  val cookie = """DoaaDisclaimerAccepted="DOAA DISCLAIMER READ AND ACCEPTED.""""

  val options = List(
    HttpOptions.connTimeout(1000),
    HttpOptions.readTimeout(5000)
  )

  def urlTransform(url: String): String =
    if (url.startsWith("/")) "http://open.georgia.gov%s".format(url) else url

  def get(url: String): Request =
    Http(urlTransform(url))
      .cookie(cookie)
      .options(options)

  def post(url: String, data: String): Request =
    Http.postData(urlTransform(url), data)
      .cookie(cookie)
      .options(options)

  def encode(data: Map[String, String]): String =
    data.map(x =>
      URLEncoder.encode(x._1, "UTF-8") + "=" +
      URLEncoder.encode(x._2, "UTF-8")
    ).mkString("&")

}
package http {

import util.matching.Regex

object Implicits {

    implicit class RichRequest(request: Request) {

      def cookie(cookie: String): Request =
        request.header("Cookie", cookie)

    }

    implicit class Headers(headers: Map[String, List[String]]) {

      def cookie(name: String): Option[String] = {
        val pattern = new Regex("%s=([^;]+);.*".format(name))
        headers("Set-Cookie").flatMap(_ match {
          case pattern(jsi) => List(jsi)
          case _ => Nil
        }).headOption
      }

    }

  }

}
