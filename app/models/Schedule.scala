package models

import java.time.LocalDateTime

case class Schedule(id: Long, name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime)

object Schedule {
  def apply(name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime): Schedule = {
    Schedule(0, name, startDateTime, endDateTime)
  }
}
