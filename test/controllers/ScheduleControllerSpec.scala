package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class ScheduleControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "ScheduleController GET" should {

    "render the index page from a new instance of controller without date" in {
      val controller = new ScheduleController(stubControllerComponents(), app.injector.instanceOf[CSRFAddToken], app.injector.instanceOf[CSRFCheck])
      val home = controller.get(None).apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Schedule")
    }

    "render the index page from a new instance of controller with date" in {
      val controller = new ScheduleController(stubControllerComponents(), app.injector.instanceOf[CSRFAddToken], app.injector.instanceOf[CSRFCheck])
      val home = controller.get(Some("2023-01-01")).apply(FakeRequest(GET, "/?date=2023-01-01"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Schedule")
    }

    "render the index page from the application" in {
      val controller = inject[ScheduleController]
      val home = controller.get(None).apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Schedule")
    }

    "reander the add page from a new instance of controller" in {
      val controller = new ScheduleController(stubControllerComponents(), app.injector.instanceOf[CSRFAddToken], app.injector.instanceOf[CSRFCheck])
      val add = controller.add().apply(FakeRequest(GET, "/add"))

      status(add) mustBe OK
      contentType(add) mustBe Some("text/html")
      contentAsString(add) must include ("Add Your Schedule")
    }
  }

  "ScheduleController POST" should {

    "add a new schedule" in {
      val controller = new ScheduleController(stubControllerComponents(), app.injector.instanceOf[CSRFAddToken], app.injector.instanceOf[CSRFCheck])
      val request = FakeRequest(POST, "/add")
        .withFormUrlEncodedBody("name" -> "Meeting", "startDateTime" -> "2023-01-01T10:00", "endDateTime" -> "2023-01-01T11:00")
      val add = controller.post().apply(request)

      status(add) mustBe SEE_OTHER
      redirectLocation(add) mustBe Some("/")
    }

    "add a new schedule with invalid form" in {
      val controller = new ScheduleController(stubControllerComponents(), app.injector.instanceOf[CSRFAddToken], app.injector.instanceOf[CSRFCheck])
      val request = FakeRequest(POST, "/add")
        .withFormUrlEncodedBody("name" -> "Meeting", "startDateTime" -> "2023-01-01T10:00", "endDateTime" -> "2023-01-01T09:00")
      val add = controller.post().apply(request)

      status(add) mustBe BAD_REQUEST
      contentType(add) mustBe Some("text/html")
      contentAsString(add) must include ("End date must be after start date")
    }
  }
}
