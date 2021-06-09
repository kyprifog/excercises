
//Given a list of numbers [1,2,3,4,5,6,7,8,9], print all the pairs for which the summation of index positions is equal to 10

object TargetSum {

  def run(list: List[Int], target: Int) = {
    list.foldLeft((List[Int](), List[(Int, Int)]())){case(acc, n) =>
      val matches = acc._1.map{r => (r, n, r - target + n)}.filter(_._3 == 0).map{r => (r._1, r._2)}
      val newMatches = acc._2 ++ matches
      val newList = (acc._1.filter{r => !matches.map(_._1).contains(r)}) ++ List(n)
      (newList, newMatches)
    }
  }

}

assert(TargetSum.run(List(1,2,3,4,5,6,7,8,9), 10)._2 == List((4,6), (3,7),(2,8),(1,9)))
assert(TargetSum.run(List(1,9), 10)._2 == List((1,9)))
assert(TargetSum.run(List(1,1), 10)._2 == List())
