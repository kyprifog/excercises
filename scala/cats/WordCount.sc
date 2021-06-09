
import cats.effect.{IO, Resource}
import scala.io.{BufferedSource, Source}
import java.io.FileWriter


object WordCount {

  def run(input: String, output: String): Unit = {

    def countWords(file: BufferedSource): List[(String, Int)] = file.getLines()
      .map(_.trim.toLowerCase)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .foldLeft(Map.empty[String, Int]) { (m, n) =>
        val nn = (n, m.getOrElse(n, 0) + 1)
        m + nn
      }
      .toList
      .sortBy(-_._2)
      .slice(0, 20)

    lazy val program: IO[Unit] = {
      (for {
        reader <- Resource.fromAutoCloseable(IO(Source.fromFile(input)))
        writer <- Resource.fromAutoCloseable(IO(new FileWriter(output)))
      } yield (reader, writer)).use { case (reader, writer) =>
        val counts = countWords(reader)
        for {c <- counts} {
          val data = c._1 + ":" + c._2
          println(data)
          writer.write(data + "\n")
        }
        IO(println("-" * 60 + "\n" + f"Top 20 Results saved to: ${output}"))
      }
    }

    program.unsafeRunSync()
  }
}


WordCount.run("/Users/kyle/dev/excercises/data/pg1787.txt", "/tmp/wordcount/results.txt")



