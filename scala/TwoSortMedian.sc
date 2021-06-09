
object TwoSortMedian {
  // Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
  // The overall run time complexity should be O(log (m+n)).

  def run(a1: Array[Int], a2: Array[Int]): Double = {
    val total = (a1 ++ a2).sorted
    val len = total.length
    val mid = (len.toDouble / 2)
    val midIndex =  mid.toInt
    val first = total.lift(midIndex).getOrElse(0)
    val second = if (mid.isWhole) { total.lift(midIndex - 1).getOrElse(0) } else {first}
    (first.toDouble + second.toDouble) / 2
  }
}

assert(TwoSortMedian.run(Array(1,2), Array(2)) == 2.0)
assert(TwoSortMedian.run(Array(1,2), Array(3,4)) == 2.5)
assert(TwoSortMedian.run(Array(0,0), Array(0,0)) == 0.0)
assert(TwoSortMedian.run(Array(), Array(1)) == 1.0)
assert(TwoSortMedian.run(Array(2), Array()) == 2.0)

