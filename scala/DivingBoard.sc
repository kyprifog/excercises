//Diving  Board:  You  are  building a  diving  board  by  placing a  bunch of planks  of wood  end-to-end.
//There  are  two  types of  planks,  one  of length shorter  and  one  of length  longer.
//You  must  use  exactly  K  planks of wood. Write a method to generate all possible lengths for the diving board.

object DivingBoard {

  def run(numPlanks: Int, longLength: Double, shortLength: Double): List[Double] = {
    val long = ((0 to numPlanks).toList).map{n => longLength * n}
    val short = ((0 to numPlanks).toList).map{n => shortLength * n}
    long.reverse.zip(short).map{case(l,s) => l+s}.sorted
  }

}

assert(DivingBoard.run(0, 0, 1) == List(0.0))
assert(DivingBoard.run(1, 2, 1) == List(1,2))
assert(DivingBoard.run(4, 5, 2) == List(8.0, 11.0, 14.0, 17.0, 20.0))
