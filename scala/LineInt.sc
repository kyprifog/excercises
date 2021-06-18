case class Point2(x: Double, y: Double)
case class Line2(start: Point2, end: Point2)

object LineInt {

  def slopeIntercept(line: Line2): (Double, Double) = {
    val slope = (line.end.y - line.start.y) / (line.end.x - line.start.x)
    val intercept = line.start.y - (line.start.x * slope)
    (slope, intercept)
  }

  def isOnLine(line: Line2, point: Point2): Boolean = {
    val xSort = List(line.start.x, line.end.x).sorted
    val ySort = List(line.start.y, line.end.y).sorted
    val a = (xSort(0) <= point.x)
    val b = (xSort(1) >= point.x)
    val c = (ySort(0) <= point.y)
    val d = (ySort(1) >= point.y)
    a && b && c && d
  }

  def run(line1: Line2, line2: Line2): List[Point2] = {
    val (slope1, intercept1) = slopeIntercept(line1)
    val (slope2, intercept2) = slopeIntercept(line2)

    val (x,y): (Option[Double], Option[Double]) = if (math.abs(slope1) == Double.PositiveInfinity) {
      if (math.abs(slope2) == Double.PositiveInfinity) {
        // TODO:  intersect along segment or on endpoint
        (None, None)
      } else {
        if (intercept1 == Double.NaN) {
          (Some(0.0), Some(intercept2))
        } else {
          (Some(line1.start.x), Some((slope2 * (line1.start.x)) + intercept2))
        }
      }
    } else {
      if (math.abs(slope2) == Double.PositiveInfinity) {
        if (intercept2 == Double.NaN) {
          (Some(0.0), Some(intercept1))
        } else {
          (Some(line2.start.x), Some((slope1) * (line2.start.x) + intercept1))
        }
      } else {
        if (slope1 == slope2) {
          // TODO: Intersect along segment or on endpoint
          (None, None)
        } else {
          val x = (intercept2 - intercept1) / (slope1 - slope2)
          val y = (slope1 * x) + intercept1
          (Some(x), Some(y))
        }
      }
    }
    (x,y) match {
      case (Some(x), Some(y)) =>  {
        val p = Point2(x,y)
        if (isOnLine(line1, p) && isOnLine(line2, p)) { List(p) } else {List()}
      }
      case _ => List()
    }
  }

}

val l1 = Line2(Point2(0.0, 0.0), Point2(1.0, 1.0))
val l2 = Line2(Point2(0.0, 1.0), Point2(1.0, 0.0))
assert(LineInt.run(l1, l2) == List(Point2(0.5,0.5)))

val l3 = Line2(Point2(-1.0, -1.0), Point2(1.0, 1.0))
val l4 = Line2(Point2(-1.0, 1.0), Point2(1.0, -1.0))
assert(LineInt.run(l3, l4) == List(Point2(0.0,0.0)))

val l5 = Line2(Point2(-1.0, 0.0), Point2(0.0, 1.0))
val l6 = Line2(Point2(0.0, 0.0), Point2(1.0, 1.0))
assert(LineInt.run(l5, l6) == List())

val l7 = Line2(Point2(0.0, 0.0), Point2(1.0, 1.0))
val l8 = Line2(Point2(2.0, 1.0), Point2(1.0, 2.0))
assert(LineInt.run(l7, l8) == List())

val l9 = Line2(Point2(0.0, -1.0), Point2(0.0, 1.0))
val l10 = Line2(Point2(-1.0, 0.0), Point2(1.0, 0.0))
assert(LineInt.run(l9, l10) == List(Point2(0.0, 0.0)))

val l11 = Line2(Point2(0.0, -1.0), Point2(0.0, 1.0))
val l12 = Line2(Point2(-1.0, 1.0), Point2(1.0, 1.0))
assert(LineInt.run(l11, l12) == List(Point2(0.0, 1.0)))

val l13 = Line2(Point2(0.0, -1.0), Point2(0.0, 1.0))
val l14 = Line2(Point2(-1.0, -2.0), Point2(1.0, 1.0))
assert(LineInt.run(l13, l14) == List(Point2(0.0, -0.5)))

val l15 = Line2(Point2(-1.0, 0.0), Point2(1.0, 0.0))
val l16 = Line2(Point2(1.0, -2.0), Point2(1.0, 1.0))
assert(LineInt.run(l15, l16) == List(Point2(1.0, 0.0)))
