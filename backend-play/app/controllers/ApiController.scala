package controllers

import javax.inject._

import models.Person
import play.api.mvc._
import play.api.libs.json._

@Singleton
class ApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def persons() = Action { implicit request: Request[AnyContent] => {

    val dummyResponse = Seq(Person("Fred"), Person("Harry"))
    Ok(Json.toJson(dummyResponse))
  }}
}
