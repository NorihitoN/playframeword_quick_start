// Test using Selenium WebDriver
// Use HtmlUnitDriver for headless testing

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.mvc._
import play.api.inject.guice._
import play.api.routing._
import play.api.routing.sird._

class MyScheduler extends PlaySpec with GuiceOneServerPerTest with OneBrowserPerSuite with HtmlUnitFactory {

  import org.openqa.selenium.htmlunit.HtmlUnitDriver
  import com.gargoylesoftware.htmlunit.BrowserVersion

  override def fakeApplication() = {
    new GuiceApplicationBuilder()
      .configure(
        "db.default.url" -> "jdbc:h2:mem:test;MODE=MYSQL",
        "db.default.driver" -> "org.h2.Driver")
      .build()
  }

  override def createWebDriver() = {
    val driver = new HtmlUnitDriver(BrowserVersion.BEST_SUPPORTED, true){
      def setAcceptLanguage(language: String) = {
        this.getWebClient().addRequestHeader("Accept-Language", language)
      }
    }
    driver.setJavascriptEnabled(true)
    driver.setAcceptLanguage("en")
    driver
  }

  "GET /" should {
    "work from within a browser" in {
      go to s"http://localhost:$port/"
      assert((pageTitle == "Your Schedule"))
    }
  }

  "GET /add" should {
    "work from within a browser" in {
      go to s"http://localhost:$port/add"
      assert((pageTitle == "Add Your Schedule"))
    }
  }

  "POST /add" should {
    "work from within a browser" in {
      go to s"http://localhost:$port/add"
      textField("name").value = "Meeting"

      // val startDateTimeField = find(name("startDateTime")).get
      // startDateTimeField.underlying.clear()
      // startDateTimeField.underlying.sendKeys("2021-01-01T10:00")
      // 
      // val endDateTimeField = find(name("endDateTime")).get
      // endDateTimeField.underlying.clear()
      // endDateTimeField.underlying.sendKeys("2021-01-01T11:00")
      //
      // Set datetime-local input fields using JavaScript
      executeScript("document.getElementsByName('startDateTime')[0].value = '2021-01-01T10:00'")
      executeScript("document.getElementsByName('endDateTime')[0].value = '2021-01-01T11:00'")

      submit()

      eventually {
        assert((pageTitle == "Your Schedule"))
      }
    }

    "work from within a browser with invalid input" in {
      go to s"http://localhost:$port/add"
      textField("name").value = "Meeting"

      executeScript("document.getElementsByName('startDateTime')[0].value = '2021-01-01T10:00'")
      executeScript("document.getElementsByName('endDateTime')[0].value = '2021-01-01T09:00'")

      submit()

      eventually {
        val error = find(cssSelector("p.text-danger")).get
        assert((error.text == "End date must be after start date"))
        assert((pageTitle == "Add Your Schedule"))
      }
    }
  }
}
