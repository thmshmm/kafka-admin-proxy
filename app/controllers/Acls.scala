package controllers

import javax.inject.Inject
import play.api.mvc._

class Acls @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def getAcls() = TODO
}
