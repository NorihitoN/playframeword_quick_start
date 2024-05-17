import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.mvc._
import play.api.inject.guice._
import play.api.test.WsTestClient
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MySchedulerApi extends PlaySpec with GuiceOneServerPerTest {

  override def fakeApplication() = {
    new GuiceApplicationBuilder()
      .configure(
        "db.default.url" -> "jdbc:h2:mem:test;MODE=MYSQL",
        "db.default.driver" -> "org.h2.Driver")
      .build()
  }

  "GET /schedules" should {
    "return 200" in {
      WsTestClient.withClient { client =>
        val response = Await.result(client.url(s"http://localhost:$port/schedules").get(), Duration.Inf)
        assert(response.status == 200)
      }
    }
  }

  "POST /schedules" should {
    "return 200" in {
      WsTestClient.withClient { client =>
        val response = Await.result(client.url(s"http://localhost:$port/schedules").post(Map("name" -> Seq("test"), "startDateTime" -> Seq("2020-01-01T00:00"), "endDateTime" -> Seq("2020-01-01T01:00"))), Duration.Inf)
        assert(response.status == 200)
      }
    }

    "return 400" in {
      WsTestClient.withClient { client =>
        val response = Await.result(client.url(s"http://localhost:$port/schedules").post(Map("name" -> Seq("test"), "startDateTime" -> Seq("2020-01-01T00:00"), "endDateTime" -> Seq("2019-01-01T01:00"))), Duration.Inf)
        assert(response.status == 400)
      }
    }
  }
}
