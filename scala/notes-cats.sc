
import cats.Semigroup
import cats.implicits._

Semigroup[Int].combine(1, 2)
Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
Semigroup[Option[Int]].combine(Option(1), None)
Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)

Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map()))
Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42)))

val aMap = Map("foo" → Map("bar" → 5))
val anotherMap = Map("foo" → Map("bar" → 6))
val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)

// Type classes add ad hoc polymorphism to scala

trait Show[A] {
  def show(a: A): String
}
//
//object Show {
//  def show[A](a: A)(implicit sh: Show[A]) = sh.show(a)
////  also:
////  def show[A: Show](a: A) = implicitly[Show[A]].show(a)
//  // or
//// def apply[A](implicit sh: Show[A]): Show[A] = sh
//  // def show[A: Show](a: A) = Show.apply[A].show(a) which simplifies to
//// def show[A: Show](a: A) = Show[A].show(a)
//
//
//  val intCanShow: Show[Int] =
//    new Show[Int] {
//      def show(int: Int): String = s"int $int"
//    }
//}
//
//import Show._
//
//println(intCanShow.show(20))

//println(show(20)) // implicit def allows this

// type class basically a trait with a type, and an implementation for each type
object Show {
  def apply[A](implicit sh: Show[A]): Show[A] = sh

  def show[A: Show](a: A) = Show[A].show(a)

  implicit class ShowOps[A: Show](a: A) {
    def show = Show[A].show(a)
  }

  implicit val intCanShow: Show[Int] =
    new Show[Int] {
      def show(int: Int): String = s"int $int"
    }
}
implicit val stringCanShow: Show[String] =
  new Show[String]{
    def show(str: String): String = s"string $str"
  }
println(30.show)

// Monoid -> semigroup + empty class
// Functor -> Types that have a "hole", F[?] for example, Option, List
// Apply -> extends functor  type class with new function ap similar to map but operates on F[A => B] instead of A => B
// Applicative -> adds "pure" function which moves A => F[A], for example 1 => Option(1)
// Monad -> adds "flatten"