import com.spotify.scio.ScioContext

object WordCount {
  def run(input: String, output: String): Unit = {
    val sc = ScioContext()

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
val inputFile = "/Users/kyle/dev/excercises/data/hamlet.txt"

WordCount.run(inputFile, "/tmp/wordcount/")

  //STDOUT
  //  (the,971)
  //  (and,723)
  //  (of,671)
  //  (to,656)
  //  (I,549)
  //  (you,503)
  //  (a,479)
  //  (my,444)
  //  (in,404)
  //  (it,367)
  //  (Ham,358)
  //  (is,337)
  //  (not,302)
  //  (his,286)
  //  (And,273)
  //  (that,270)
  //  (this,257)
  //  (with,238)
  //  (me,234)
  //  (your,225)
