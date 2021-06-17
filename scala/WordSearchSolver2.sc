
// Version that
object WordSearchSolver2 {

  def getNeighbors(i: Int, j: Int, matrix: List[(String, Int, Int)]): List[(String, Int, Int)] = {
    def get(i: Int, j: Int): Option[(String, Int, Int)] = {
      matrix.find{r => r._2 == i && r._3 == j}
    }

    // To add diagonals all you have to do is add cross components i+-1, j+-1 here

    List(
      get(i+1,j),
      get(i-1,j),
      get(i,j+1),
      get(i,j-1),
    ).flatten
  }

  def exist(board: Array[Array[Char]], word: String): Boolean = {
    val max: Int = word.length
    val matrix: List[(String, Int, Int)] =
      for {
        (row, i) <- board.toList.zipWithIndex
        (char,j) <- row.toList.zipWithIndex
      } yield ( (char.toString, i, j) )


    (1 until max).foldLeft((matrix, false)) { case(acc, n)  =>
      val prevIt: List[(String, Int, Int)] = acc._1
      val matches: Boolean = acc._2

      val neighbors: List[(String, Int, Int)] = prevIt
        .flatMap{m =>
          getNeighbors(m._2, m._3, matrix).map(r => (f"${m._1}${r._1}", r._2, r._3))
        }

      val newMatches: Boolean = matches || neighbors.map(_._1).contains(word)
      val nextIt = neighbors.filter(f => word.startsWith(f._1))
      (nextIt, newMatches)
    }._2
  }
}

val grid = Array(
  Array('F', 'H', 'O', 'P'),
  Array('O', 'B', 'C', 'A'),
  Array('U', 'E', 'K', 'R'),
  Array('G', 'Y', 'E', 'T')
)


assert(WordSearchSolver.exist(grid, "ART") == true)
assert(WordSearchSolver.exist(grid, "BAD") == false)
assert(WordSearchSolver.exist(grid, "FOBO")== false)
