package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def untrail(path: String) = Action {
    MovedPermanently("/%s".format(path))
  }

  def index = Action {
    Ok(views.html.index())
  }

}
