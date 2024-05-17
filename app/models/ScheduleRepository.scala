package models
import java.time.LocalDateTime

object ScheduleRepository {

  var schedules: List[Schedule] = List(
    Schedule(1, "Meeting", LocalDateTime.of(2021, 1, 1, 10, 0), LocalDateTime.of(2021, 1, 1, 11, 0)),
    Schedule(2, "Recruit", LocalDateTime.of(2024, 5, 17, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0))
  )

  def findAll: List[Schedule] = schedules

  def find(date: Option[String]): List[Schedule] = {
    date match {
      case Some(data) => schedules.filter(_.startDateTime.toLocalDate.toString == data)
      case None => schedules.filter(_.startDateTime.toLocalDate == LocalDateTime.now().toLocalDate)
    }

  }

  def add(schedule: Schedule): Unit = schedules = schedules :+ schedule
}
