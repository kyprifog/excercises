
class WindowedAverager(window: Int) {

  var stack: List[Int] = List()
  var prevSum: Int = 0
  def accept(x: Int): Double = {

    val remove = if (stack.length >= window) {
      stack.head
    } else {
      0
    }

    prevSum = prevSum - remove + x
    stack = (stack ++ List(x)).takeRight(window)
    val len = stack.length
    prevSum.toDouble / len.toDouble
  }

}

val wa = new WindowedAverager(3)


assert(wa.accept(1) == 1.0)
assert(wa.accept(2) == 1.5)
assert(wa.accept(3) == 2.0)
assert(wa.accept(4) == 3.0)

