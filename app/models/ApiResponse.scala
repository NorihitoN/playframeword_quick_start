package models

import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.json.JsValue

case class Meta(status: Int, errorMessage: Option[String] = None)

object Meta {
  implicit val writes: Writes[Meta] = Json.writes[Meta]
}

case class ApiResponse(meta: Meta, data: Option[JsValue] = None)

object ApiResponse {
  implicit def writes: Writes[ApiResponse] = Json.writes[ApiResponse]
}
