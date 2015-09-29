package com.sky.assignment

import spray.json.DefaultJsonProtocol

/**
 * Created by virendrajha on 27/09/2015.
 */

case class Recommendation(uuid: String, start: Long, end: Long)

case class Recommendations(recommendations: List[Recommendation])

case class RecommendationsResult(recommendations: List[Recommendation], expiry: Long)

object RecommendationJsonFormat extends DefaultJsonProtocol {

	implicit val recommendationFormat = jsonFormat3(Recommendation)
	implicit val recommendationsFormat = jsonFormat1(Recommendations)
	implicit val recommendationsResult = jsonFormat2(RecommendationsResult)

}