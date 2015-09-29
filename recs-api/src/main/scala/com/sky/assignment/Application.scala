package com.sky.assignment

import akka.actor.{ActorSystem, Props}
import spray.routing.SimpleRoutingApp

object Application extends App with SimpleRoutingApp {

	implicit val system = ActorSystem("recs")

	startServer(interface = "localhost", port = 8090) {
		path("personalised" / Segment) { subscriber =>
			requestContext =>
				val recommendationService = system.actorOf(Props(new RecommendationService(requestContext)))
				println(subscriber)
				recommendationService ! RecommendationService.Process(5, subscriber)
		}
	}
}




