import scala.io.Source

object InvertedIndex {
  case class Document(id: Int, entries: Seq[String])
  case class Index(s: String, indices: List[Int]) {
    def print(): Unit = {
      println(s"${s} = [${indices.mkString(",")}]")
    }
  }

  // Note: Simulating documents by just evenly splitting a single document
  def run(path: String) = {
    val noGroups = 1000
    val source = Source.fromFile(path)
    val data = source.getLines().toList
    val l = data.length
    val groupSize = (l / noGroups)

    val documents = data
      .map(_.trim.toLowerCase)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))
      .grouped(groupSize)
      .zipWithIndex
      .map{r => Document(r._2, r._1)}

    val indices = documents
      .flatMap{r1 => r1.entries.map{r2 => (r2, r1.id)}}.toList
      .groupBy(_._1)
      .mapValues{r => r.map{r => r._2}}
      .map{case(k,v) => Index(k, v)}.toList
      .sortBy(i => -i.indices.length)

    indices
      .filter(_.indices.length == 10)
      .sortBy(_.s)
      .take(10)
      .map(_.print())
  }

}
InvertedIndex.run("/Users/kyle/dev/excercises/data/kinglear.txt")