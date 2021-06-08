
import scala.io.Source

object WordCount {
  def run(input: String): Unit = {
    val file = Source.fromFile(input)
    val col = file.getLines.toList
      .map(_.trim.toLowerCase)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .foldLeft(Map.empty[String, Int]) { (m, n) =>
          val nn = (n, m.getOrElse(n, 0) + 1)
          m + nn }
      .toList
      .sortBy(-_._2)
      .slice(0,20)

    for (c <- col) {
      println(c)
    }
  }
}



WordCount.run("/Users/kyle/dev/excercises/data/pg1787.txt")



