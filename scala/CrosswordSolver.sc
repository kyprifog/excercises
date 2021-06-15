class CrosswordSolver(grid: List[List[String]]) {

  val matrix: List[(String, Int, Int)] =
    for {
      (row, i) <- grid.zipWithIndex
      (string,j) <- row.zipWithIndex
    } yield ( (string, i, j) )

  def getNeighbors(i: Int, j: Int): List[(String, Int, Int)] = {
    def get(i: Int, j: Int): Option[(String, Int, Int)] = {
      matrix.find{r => r._2 == i && r._3 == j}
    }

    List(
      get(i+1,j),
      get(i-1,j),
      get(i,j+1),
      get(i,j-1),
    ).flatten
  }

  def run(dictionary: List[String]): List[String] = {
    val max: Int = dictionary.map(_.length).max
    (1 until max).foldLeft((matrix, List[String]())) { case(acc, n)  =>
      val prevIt = acc._1
      val matches = acc._2

      val neighbors = prevIt
        .flatMap{m =>
          getNeighbors(m._2, m._3).map(r => (f"${m._1}${r._1}", r._2, r._3))
        }

      val newMatches = matches ++ neighbors.map(_._1).filter{f => dictionary.map(d => f == d).foldLeft(false)(_||_) }
      val nextIt = neighbors.filter(f => dictionary.map(d => d.startsWith(f._1)).foldLeft(false)(_||_))
      (nextIt, newMatches)
    }._2.toSet.toList
  }
}

val grid = List(
  List("F", "H", "O", "P"),
  List("O", "B", "C", "A"),
  List("U", "E", "K", "R"),
  List("G", "Y", "E", "T")
)

val dictionary = List(
  "ART",
  "BEEN",
  "CARE",
  "CONTINENT",
  "DIVISION",
  "EXPERIMENT",
  "EYE",
  "FIND",
  "GRAND",
  "HOCKEY",
  "HOP",
  "HUNT",
  "LETTER",
  "MILK",
  "NUMERAL",
  "PAR",
  "PART",
  "PLACE",
  "REACH",
  "SECTION",
  "SLEEP",
  "STRING",
  "THIS",
  "TRAP",
  "TRAVEL",
  "VERY",
  "WONDER",
  "YET"
)


val result = List("ART", "PAR", "EYE", "HOP", "TRAP", "HOCKEY", "YET", "PART")
val exp = new CrosswordSolver(grid).run(dictionary)
assert(exp == result)
