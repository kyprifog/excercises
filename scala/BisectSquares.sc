//Bisect Squares: Given two squares on a two-dimensional plane,
//find a line that would cut these two squares in  half.
//Assume that the top and the bottom  sides of the square run parallel to the x-axis.

case class Point(x: Double, y: Double)
case class Square(bottomLeft: Point, width: Double)
case class Line(slope: Double, yInt: Double)

object BisectSquares {

  def run(square1: Square, square2: Square) : Line= {
    def getCenter(square: Square): Point = {
      val x = square.bottomLeft.x + (square.width / 2)
      val y = square.bottomLeft.y + (square.width / 2)
      Point(x,y)
    }
    val center1 = getCenter(square1)
    val center2 = getCenter(square2)
    val slope = (center2.y - center1.y) / (center2.x - center1.x)
    val intercept = center1.y - slope * center1.x
    Line(slope, intercept)
  }

}

assert(BisectSquares.run(Square(Point(0.0,0.0), 1.0), Square(Point(1.0,1.0), 1.0)) == Line(1.0, 0))
assert(BisectSquares.run(Square(Point(0.0,0.0), 3.0), Square(Point(4.0,0.0), 3.0)) == Line(0.0, 1.5))
assert(BisectSquares.run(Square(Point(0.0,0.0), 3.0), Square(Point(0.0,4.0), 3.0)) == Line(Double.PositiveInfinity, Double.NegativeInfinity))
