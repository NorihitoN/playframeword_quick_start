package models
import java.time.LocalDateTime
import scalikejdbc._

object ScheduleRepository {

  var schedules: List[Schedule] = List(
    Schedule(1, "Meeting", LocalDateTime.of(2021, 1, 1, 10, 0), LocalDateTime.of(2021, 1, 1, 11, 0)),
    Schedule(2, "Recruit", LocalDateTime.of(2024, 5, 17, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0))
  )

  def find(date: Option[String]): List[Schedule] = DB readOnly { implicit session =>
    date match {
      case Some(date) => sql"SELECT * FROM schedules WHERE start_date <= ${date} AND end_date >= ${date} order by start_date desc".map(rs => Schedule(rs.int("id"), rs.string("name"), rs.localDateTime("start_date"), rs.localDateTime("end_date"))).list.apply()
      case None => sql"SELECT * FROM schedules order by start_date desc".map(rs => Schedule(rs.int("id"), rs.string("name"), rs.localDateTime("start_date"), rs.localDateTime("end_date"))).list.apply()
    }
  }

  def add(schedule: Schedule): Unit = DB localTx { implicit session =>
    sql"INSERT INTO schedules (name, start_date, end_date) VALUES (${schedule.name}, ${schedule.startDateTime}, ${schedule.endDateTime})".update.apply()
  }
}
