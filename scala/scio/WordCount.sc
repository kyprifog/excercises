import com.spotify.scio.ContextAndArgs

object WordCount {
  def main(cmdlineArgs: Array[String]): Unit = {
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    val input = args("input")
    val output = args("output")

    implicit def ord: Ordering[(String, Long)] = Ordering.apply(_._2 compare _._2)

    val col = sc.textFile(input)
      .map(_.trim)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .countByValue
      .top(20)
      .flatMap(r => r.toList)
      .tap(a => println(a))
      .map(r => r._1 + ":" + r._2)

    col.saveAsTextFile(output)
    val result = sc.run().waitUntilFinish()
    println(result.state.name())
  }
}


//val inputFile = "https://www.gutenberg.org/cache/epub/1787/pg1787.txt"
val inputFile = "/Users/kyle/dev/excercises/data/pg1787.txt"

WordCount.main(Array(f"--input=${inputFile}", f"--output=/tmp/wordcount/"))



