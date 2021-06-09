object TwoSum {
  // Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
  def run(nums: Array[Int], target: Int): Option[(Int, Int)] = {
    val numWithIndex = nums.zipWithIndex
    val diffs: Array[(Int, Int)] = numWithIndex.map{x => (target - x._1, x._2)}

    def checkMatch(acc: Option[(Int, Int)], value: (Int, Int)): Option[(Int, Int)] = {
      acc match {
        case Some(v) => Some(v)
        case _ => {
          numWithIndex.find(n => n._1 == value._1 && n._2 != value._2).map(v => (value._2, v._2))
        }
      }
    }

    val zero: Option[(Int, Int)] = None
    diffs.foldLeft(zero)(checkMatch)
  }
}

assert(TwoSum.run(Array(1,2,3), 3) == Some(0,1))
assert(TwoSum.run(Array(1,2,3), 9).isEmpty)
assert(TwoSum.run(Array(0,9,1), 10) == Some(1,2))
assert(TwoSum.run(Array(1,1,3,1,8), 11) == Some(2,4))
assert(TwoSum.run(Array(3,4,2), 6) == Some(1,2))

