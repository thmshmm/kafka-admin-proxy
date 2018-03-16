package utils

import play.api.libs.json.{JsObject, JsString}


case class JsonResponse(val code: Int, final val msg: String) {
  override def toString = JsObject(Seq("msg" -> JsString(msg))) toString
}

object JsonResponse {
  def apply(code: Int, msg: String): JsonResponse = new JsonResponse(code, msg)

  def code: Int = this.code

  def msg: String = this.msg
}
