import faker.Lorem._
import scala.io.Source

object WordCount{
  def run(s: Stream[String]): Unit = {
    s.flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .map((_,1))
      .groupBy(_._1)
      .mapValues{r => r.map(_._2).sum}
      .toList.sortBy(-_._2)
      .take(20)
      .foreach{case(s, i) => println(s"${s} : ${i}")}
  }
}

val s1 = Stream("the", "rain", "in", "main", "stays", "mainly", "on", "the", "plain", "plain", "plain")

WordCount.run(s1)

def generate(i: Int) = {
  (1 to i).toStream.map(paragraph)
}

val s2 = s1 ++ generate(100)

WordCount.run(s2)
val s = Source.fromFile("/Users/kyle/dev/excercises/data/kinglear.txt").getLines().toStream

WordCount.run(s)

val stdin = Source.stdin.getLines().toStream

WordCount.run(stdin)

