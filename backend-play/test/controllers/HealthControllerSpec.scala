package controllers

import org.mockito.Mockito
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, TestData}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Application
import play.api.db.{DBApi, DBModule}
import play.api.db.evolutions.EvolutionsModule
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._


class HealthControllerSpec extends PlaySpec with GuiceOneAppPerTest with ScalaFutures with BeforeAndAfterAll with MockitoSugar {
  "Routes" should {

    "have a nice health endpoint that works when everything is ok" in  {
      route(app, FakeRequest(GET, "/health")).map(status(_)) mustBe Some(OK)
    }
  }

}
