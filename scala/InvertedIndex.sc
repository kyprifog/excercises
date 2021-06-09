import scala.io.Source

class InvertedIndex(path: String) {
  case class Document(id: Int, entries: Seq[String])
  case class Index(s: String, indices: List[Int]) {
    def print(): Unit = {
      println(s"${s} = [${indices.mkString(",")}]")
    }
  }

  // Note: Simulating documents by just evenly splitting a single document
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

  def get(s: String) = {
    indices.find(_.s == s).map(_.indices).getOrElse(List())
  }

}
val index = new InvertedIndex("/Users/kyle/dev/excercises/data/kinglear.txt")
assert(index.get("castle") == List(1052,2305,2584,2974,3949,4396,4449,4683,5507,7170))
assert(index.get("gives") == List(1684,2071,2957,3114,3119,4116,4261,4403,5172,6689))
assert(index.get("machine") == List(28,173,322,350,390,2293,3635,4935,6422,7369))
assert(index.get("Prifogle") == List())
