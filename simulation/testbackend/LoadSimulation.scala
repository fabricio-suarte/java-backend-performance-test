package testbackend

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

object GetCustomerList {

  var list = exec(http("GetCustomerList")
    .get("/customer")
    .check(status is 200))
}

object CreateNewCustomer {

  var customer = exec( http("CreateNewCustomer")
    .post("/customer")
    .header("Content-Type", "application/json")
    .body( StringBody( """{"name": "Customer X", "address": "Address X", "birthDate": 862029931759}""") ).asJson
    .check(status is 201))
}

class LoadSimulation extends Simulation {
  var t = 30
  var usersQuerying = 42000 // 1400 requests per second
  var usersWriting = 18000 // 600 requests per second

  val httpConf = http
    .baseUrl("http://localhost:8080/test-backend")
    .acceptHeader("text/html,application/json")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0")

  val listing = scenario("Listing").exec(GetCustomerList.list)
  val registering = scenario("Registering").exec(CreateNewCustomer.customer)

  /*setUp(
    listing.inject(atOnceUsers(1)),
    registering.inject(atOnceUsers(1))
  ).protocols(httpConf)*/

  setUp(
    listing.inject( rampUsers(usersQuerying) during (t seconds) ),
    registering.inject( rampUsers(usersWriting) during (t seconds) )
  ).protocols(httpConf)

}


