import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scalaj.http._
import spray.json._

import java.io.{BufferedWriter, FileWriter}
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val scn = scenario("BasicSimulation")
    .exec(http("Get Details")
      .get("https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json")
      .check(jsonPath("$.Results[0].Mfr_CommonName").saveAs("input"))
      .check(status.is(200))
    )
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
