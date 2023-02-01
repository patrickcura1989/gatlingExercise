import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scalaj.http._
import spray.json._

import java.io.{BufferedWriter, FileWriter}
import scala.concurrent.duration._

class BasicSimulationFeeder extends Simulation {

  val dataCSV = csv("data.csv").circular

  val scn = scenario("BasicSimulation")
    .feed(dataCSV)
    .exec(http("Post Details")
      .post("https://reqres.in/api/users")
      //.header("Cookie","cookie")
      .body(PebbleFileBody("pebbleTemplate.json")).asJson
      .check(status.is(201))
    )


  setUp(
    scn.inject(
      atOnceUsers(1)
      //      constantUsersPerSec(scala.util.Properties.envOrElse("REQ_PER_SEC", "0.50").toDouble) during (scala.util.Properties.envOrElse("DURATION", "30").toInt seconds)
    )
  )
}
