package models

import java.time.LocalDateTime
import play.api.libs.json.Writes
import play.api.libs.json.Json

case class Schedule(id: Long, name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime)

object Schedule {
  implicit val writes: Writes[Schedule] = Json.writes[Schedule]

  def apply(name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime): Schedule = {
    Schedule(0, name, startDateTime, endDateTime)
  }
}
