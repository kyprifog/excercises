import com.spotify.scio.ScioContext

import scala.collection.SortedSet

object PlayCount {

  def run(minWordLength: Long, kinglear: String, hamlet: String) = {
    val sc = ScioContext()

    def getWords(path: String, play: String) = {
      sc.textFile(path)
        .map(_.trim)
        .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
        .filter(_.length > minWordLength)
        .map{w => (w.toLowerCase, play)}
    }

    val kinglearWords = getWords(kinglear, "kinglear")
    val hamletWords = getWords(hamlet, "hamlet")

    def getPart(t: (Int, String)) = t._1 match {
      case 1 => "one"
      case 2 => "both"
      case _ => "none"
    }

    kinglearWords.union(hamletWords)
      .aggregateByKey(SortedSet[String]())(_ + _, _ ++ _)
      .mapValues(_.toList)
      .tap(println(_))

    val result = sc.run().waitUntilFinish()
    result

  }


}

val path = "/Users/kyle/dev/excercises/data/"
PlayCount.run(12, path + "kinglear.txt", path + "hamlet.txt")
