object StepFinder {

  // Write a scrip that computes the number of different ways to
  // step up a set of g stairs given you can step up
  // 1 step, 2 steps or 3 steps at a time

  def find(g: Int, steps: Array[Int]): Array[Array[Int]] = {
    def cross(a1: Array[Int], a2: Array[Int]): Array[Array[Int]] = {
      a1.flatMap(x => a2.map(y => Array(x, y)))
    }

    var hits = Array(steps.filter{s => s == g})
    val crs = cross(steps, steps)
    var next = crs.filter{n => n.sum < g}
    hits = hits ++ crs.filter{n => n.sum == g}
    var i = 2

    while ((next.length > 0) & (i <= g)) {
      val newCross = next.flatMap{x => steps.map{y=> x ++ Array(y)}}
      next = newCross.filter{n => n.sum < g}
      hits = hits ++ newCross.filter{n => n.sum == g}
      i += 1
    }

    hits
  }

  def print(g: Int, steps: Array[Int]) = {
    val st = find(g, steps)
    for (s <- st) { println(s.mkString(",")) }
  }

}

val steps = Array(1,2,3)
StepFinder.print(1, steps)
// 1
StepFinder.print(2, steps)
// 2
// 1,1
StepFinder.print(3, steps)
// 3
// 1,2
// 2,1
// 1,1,1
StepFinder.print(4, steps)
//  1,3
//  2,2
//  3,1
//  1,1,2
//  1,2,1
//  2,1,1
//  1,1,1,1
StepFinder.print(7, steps)
//  1,3,3
//  2,2,3
//  2,3,2
//  3,1,3
//  3,2,2
//  3,3,1
//  1,1,2,3
//  1,1,3,2
//  1,2,1,3
//  1,2,2,2
//  1,2,3,1
//  1,3,1,2
//  1,3,2,1
//  2,1,1,3
//  2,1,2,2
//  2,1,3,1
//  2,2,1,2
//  2,2,2,1
//  2,3,1,1
//  3,1,1,2
//  3,1,2,1
//  3,2,1,1
//  1,1,1,1,3
//  1,1,1,2,2
//  1,1,1,3,1
//  1,1,2,1,2
//  1,1,2,2,1
//  1,1,3,1,1
//  1,2,1,1,2
//  1,2,1,2,1
//  1,2,2,1,1
//  1,3,1,1,1
//  2,1,1,1,2
//  2,1,1,2,1
//  2,1,2,1,1
//  2,2,1,1,1
//  3,1,1,1,1
//  1,1,1,1,1,2
//  1,1,1,1,2,1
//  1,1,1,2,1,1
//  1,1,2,1,1,1
//  1,2,1,1,1,1
//  2,1,1,1,1,1
//  1,1,1,1,1,1,1