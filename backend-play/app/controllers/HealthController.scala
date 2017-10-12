package controllers

import javax.inject._

import play.api.Logger
import play.api.mvc._

@Singleton
class HealthController @Inject()(
    controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {

  def health = Action {
    Ok("Feeling good today.")
  }

}
