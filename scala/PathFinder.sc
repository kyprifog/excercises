import scala.util.Try
// # Write a function that will traverse through all paths in an N x N matrix
// # Note: Viewing the matrix as a tree, each element in the first row can be viewed as the root.
// # When you travel down, you can either choose the left element in the next row, or the the element
// # directly underneath, or the element to the right.
// # In this case, a depth first search with each element in the first row should suffice (total of N times).
// # Example input: [[1 2 3] [4 5 6] [7 8 9]]

// 1 2 3
// 4 5 6
// 7 8 9

class PathFinder(matrix: List[List[Int]])  {

  def safeGet(i: Int, j: Int): Option[(Int, Int, List[Int])] = {
    matrix
      .zipWithIndex
      .find{case(l, i1) => i1 == i}
      .flatMap{r =>
        r._1
          .zipWithIndex
          .find{case(v, j1) => j1 == j}
          .map{s => s._1}
      }.map{r => (i,j,List(r))}
  }

  def getNeighbors(i: Int, j: Int): List[(Int, Int, List[Int])] = {
    List(
      safeGet(i+1, j-1),
      safeGet(i+1, j),
      safeGet(i+1,j+1)
    ).flatten
  }

  def run(): List[List[Int]] = {
      val indexedMatrix = for{
        (row,i) <- matrix.zipWithIndex
        (v,j) <- row.zipWithIndex
      } yield ((i,j, List(v)))

      var paths = indexedMatrix.filter(_._1 == 0)
      for (i <- 0 to matrix.length - 2) {
        paths = paths.flatMap{r =>
          getNeighbors(r._1, r._2).map{n =>  (n._1, n._2, r._3 ++ n._3)}
        }
      }
      println("Paths")
      println("-"*50)
      paths.map(_._3).map{x => println(s"${x.mkString(",")}"); x}
  }

}

val matrix = List(List(1,2,3), List(4,5,6), List(7,8,9))

new PathFinder(matrix).run()



