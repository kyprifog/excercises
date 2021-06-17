

case class Point2(x: Double, y: Double)
case class Line2(start: Point2, end: Point2)

object LineInt {


  def slopeIntercept(line: Line2): (Double, Double) = {
    val slope = (line.end.y - line.start.y) / (line.end.x - line.start.x)
    val intercept = line.start.y - (line.start.x * slope)
    (slope, intercept)
  }

  def run(line1: Line2, line2: Line2): Point2 = {
    val (slope1, intercept1) = slopeIntercept(line1)
    val (slope2, intercept2) = slopeIntercept(line2)
    println(s"S1S2:${slope1}, ${slope2}")
    println(s"I1I2:${intercept1}, ${intercept2}")
    val x = (intercept2 - intercept1) / (slope1 - slope2)
    val y = (slope1 * x) + intercept1
    Point2(x, y)
  }

}

//val l1 = Line2(Point2(0.0, 0.0), Point2(1.0, 1.0))
//val l2 = Line2(Point2(0.0, 1.0), Point2(1.0, 0.0))
//
//assert(LineInt.run(l1, l2) == Point2(0.5,0.5))

val l1 = Line2(Point2(0.0, -1.0), Point2(0.0, 1.0))
val l2 = Line2(Point2(-1.0, 0.0), Point2(1.0, 0.0))

assert(LineInt.run(l1, l2) == Point2(0.0,0.0))
