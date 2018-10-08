

sealed trait Tree[+A] {
  def size = {

  }

  def apply(leftRight)
}
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

