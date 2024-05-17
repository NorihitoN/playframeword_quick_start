package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import java.time.LocalDateTime
import play.api.data.Form
import play.api.data.Forms._
import models.{ScheduleRepository, Schedule}
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

case class ScheduleRequest(name: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime)

@Singleton
class ScheduleController @Inject()(val controllerComponents: ControllerComponents, addToken: CSRFAddToken, checkToken: CSRFCheck) extends BaseController {

  private[this] val form = Form(
    mapping(
      "name" -> nonEmptyText(1,30),
      "startDateTime" -> localDateTime("yyyy-MM-dd'T'HH:mm"),
      "endDateTime" -> localDateTime("yyyy-MM-dd'T'HH:mm")
    )(ScheduleRequest.apply)(ScheduleRequest.unapply).verifying(
      "End date must be after start date",
      schedule => schedule.endDateTime.isAfter(schedule.startDateTime)
      )
  )

  def get(date: Option[String]) = addToken(Action { implicit request: Request[AnyContent] =>
    date match {
      case Some(data) => Ok(views.html.index(ScheduleRepository.find(date)))
      case None => Ok(views.html.index(ScheduleRepository.find(None)))
    }
  })

  def add() = addToken(Action { implicit request: Request[AnyContent] =>
    Ok(views.html.add(form))
  })

  def post = addToken(Action { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      error => {
        BadRequest(views.html.add(error))
      },
      scheduleRequest => {
        val schedule = Schedule(scheduleRequest.name, scheduleRequest.startDateTime, scheduleRequest.endDateTime)
        ScheduleRepository.add(schedule)
        Redirect(routes.ScheduleController.get(None))
      }
    )
  })
}
