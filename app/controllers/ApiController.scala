package controllers

import javax.inject._
import play.api.mvc._
import java.time.LocalDateTime
import play.api.libs.json.Json
import models.ApiResponse
import models.Meta
import models.ScheduleRepository
import play.api.data.Form
import play.api.data.Forms._
import models.Schedule


@Singleton
class ApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private[this] val form = Form(
    mapping(
      "name" -> nonEmptyText(1,30),
      "startDateTime" -> localDateTime("yyyy-MM-dd'T'HH:mm"),
      "endDateTime" -> localDateTime("yyyy-MM-dd'T'HH:mm")
    )(ScheduleRequest.apply)(ScheduleRequest.unapply).verifying(
      "End date must be after start date",
      schedule => schedule.endDateTime.isAfter(schedule.startDateTime)))


  def get(date: Option[String]) = Action { implicit request: Request[AnyContent] =>
    date match {
      case Some(data) => Ok(Json.toJson(ApiResponse(Meta(200), Some(Json.obj("schedules" -> Json.toJson(ScheduleRepository.find(date)))))))
      case None => Ok(Json.toJson(ApiResponse(Meta(200), Some(Json.obj("schedules" -> Json.toJson(ScheduleRepository.find(None)))))))
    }
  }

  def post = Action { implicit request: Request[AnyContent] =>
    form.bindFromRequest().fold(
      error => {
        val errorMessage = error.errors(0).message
        BadRequest(Json.toJson(ApiResponse(Meta(400, Some(errorMessage)))))
      },
      scheduleRequest => {
        val schedule = Schedule(scheduleRequest.name, scheduleRequest.startDateTime, scheduleRequest.endDateTime)
        ScheduleRepository.add(schedule)
        Ok(Json.toJson(ApiResponse(Meta(200))))
      }
    )
  }
}
