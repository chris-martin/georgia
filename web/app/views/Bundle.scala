package views

import controllers.routes

case class Bundle(
  javascripts: Seq[String] = Nil,
  stylesheets: Seq[String] = Nil
) {

  def +(that: Bundle): Bundle =
    Bundle(
      javascripts = javascripts ++ that.javascripts,
      stylesheets = stylesheets ++ that.stylesheets
    )

}

object Bundle {

  def stylesheets(stylesheets: String*): Bundle =
    Bundle(stylesheets = stylesheets)

  def javascripts(javascripts: String*): Bundle =
    Bundle(javascripts = javascripts)

  val jquery =
    javascripts(routes.Assets.at("javascripts/jquery-1.9.0.min.js").url)

  val rickshaw =
    javascripts(routes.Assets.at("javascripts/rickshaw.js").url) +
    stylesheets(routes.Assets.at("stylesheets/rickshaw.css").url)

  val d3 =
    javascripts(routes.Assets.at("javascripts/d3.js").url)

}
