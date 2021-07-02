
import scala.util.Random

class Median(windowSize: Int = 5) {

  var stream: List[Int] = List()
  var right: List[Int] = List()
  var left: List[Int] = List()

  def next(i: Int): (List[Int], Option[Int]) = {
    stream = (stream ++ List(i))
    val l = stream.length
    stream = stream.takeRight(List(windowSize, l).min)
    if (l > 0) {
      val sorted = stream.sorted
      if (l % 2 == 0) {
        val left = sorted((l / 2) -1 )
        val right = sorted(((l/2) + 1) - 1)
        (stream, Some(List(left,right).sum / 2))
      } else {
        (stream, Some(sorted(l/2)))
      }
    } else {
      (stream,None)
    }
  }
}

val sm = new Median()

for (i <- 0 to 10)  {
  val (stream, median) =  sm.next(Random.nextInt(100))
  println(f"${median.getOrElse("None")}: ${stream.mkString(",")}")
}

