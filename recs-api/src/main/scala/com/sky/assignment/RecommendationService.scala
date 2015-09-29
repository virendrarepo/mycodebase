package com.sky.assignment

import java.util.Calendar

import akka.actor.Actor
import spray.client.pipelining._
import spray.json.DefaultJsonProtocol
import spray.routing.RequestContext

import scala.util._

/**
 * Created by virendrajha on 27/09/2015.
 */

object RecommendationService {

	case class Process(num: Int, subscriber: String)

}



class RecommendationService(requestContext: RequestContext) extends Actor {

	import com.sky.assignment.RecommendationService._

	implicit val system = context.system

	import system.dispatcher

	def receive = {

		case Process(num, subscriber) =>
			process(num, subscriber)
			context.stop(self)
	}

	def process(num: Int, subscriber: String) = {
		import com.sky.assignment.RecommendationJsonFormat._
		import spray.httpx.SprayJsonSupport._

		val pipeline = sendReceive ~> unmarshal[Recommendations]

		val now = Calendar.getInstance().getTimeInMillis
		val tuple = getConsecutivePeriods(3, now, 3600000)

		val responseFuture1 = pipeline {
			val start = tuple(0)._1
			val end = tuple(0)._2
			Get(s"http://localhost:8080/recs/personalised?num=$num&start=$start&end=$end&subscriber=$subscriber")

		}

		val responseFuture2 = pipeline {
			val start = tuple(1)._1
			val end = tuple(1)._2
			Get(s"http://localhost:8080/recs/personalised?num=$num&start=$start&end=$end&subscriber=$subscriber")
		}

		val responseFuture3 = pipeline {
			val start = tuple(2)._1
			val end = tuple(2)._2
			Get(s"http://localhost:8080/recs/personalised?num=$num&start=$start&end=$end&subscriber=$subscriber")
		}

		val aggregatedFutures = for {
			res1: Recommendations <- responseFuture1
			res2: Recommendations <- responseFuture2
			res3: Recommendations <- responseFuture2
		} yield (RecommendationsResult(res1.recommendations, tuple(0)._2), RecommendationsResult(res2.recommendations, tuple(1)._2), RecommendationsResult(res3.recommendations, tuple(2)._2))

		aggregatedFutures onComplete {
			case Success(rec) =>
				requestContext.complete(rec)
			case Failure(error) =>
				requestContext.complete(error)
		}
	}


	def getConsecutivePeriods(num: Int, start: Long, duration: Long): List[(Long, Long)] = {
		var startTime = start
		var endTime = startTime + duration
		var list = List((startTime, endTime))

		for (i <- 1 until num) {
			startTime = endTime
			endTime = startTime + duration

			list = list :+(startTime, endTime)
		}

		list
	}

}


