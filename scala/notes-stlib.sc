
// Abstract Class vs Trait

//AC: They can parameterized with constructor args
// (which traits can't) and represent a working entity.
// Traits instead just represent a single feature,
// an interface of one functionality.
// A class can inherit from multiple traits but
// only one abstract class.

// Partially Applied Functions

def sum(a: Int, b: Int, c: Int) = a + b + c

val sumC = sum(1, 10, _: Int)

def sumD(x: Int) = sum(1, 10, x)
sumC(4)
sumD(4)

def multiply(x: Int, y: Int) = x * y

val multiplyCurried = (multiply _).curried
multiplyCurried(4)(5)

val multiplyCurriedFour = multiplyCurried(4)
multiplyCurriedFour(5)

def customFilter(f: Int => Boolean)(xs: List[Int]) = xs filter f

def onlyEven(x: Int) = x % 2 == 0

val xs = List(12, 11, 5, 20, 3, 13, 2)
customFilter(onlyEven)(xs)


// Partial Functions
// has isDefinedAt and apply function
val doubleEvens: PartialFunction[Int, Int] =
  new PartialFunction[Int, Int] {
    def isDefinedAt(x: Int) = x % 2 == 0
    def apply(v1: Int) = v1 * 2
  }

//val tripleOdds: PartialFunction[Int, Int] =
//  new PartialFunction[Int, Int] {
//    def isDefinedAt(x: Int) = x % 2 != 0
//    def apply(v1: Int) = v1 *3
//  }

val tripleOdds: PartialFunction[Int, Int] = new PartialFunction[Int, Int] {
  def isDefinedAt(x: Int) = x % 3 != 0
  def apply(x: Int) = x * 3
}

val whatToDo = tripleOdds orElse doubleEvens



val whatToDo2: PartialFunction[Int, Int] =  {
  case x if (x % 2) == 0 => x * 2
  case x if (x % 2) != 0 => x * 3
}




whatToDo(3)
whatToDo(4)

whatToDo2(3)
whatToDo2(4)

val doubleEvens2: PartialFunction[Int, Int] = {
  case x if (x % 2) == 0 ⇒ x * 2
}
val tripleOdds2: PartialFunction[Int, Int] = {
  case x if (x % 2) != 0 ⇒ x * 3
}

val addFive = (x: Int) => x + 5

val whatToDo3 = doubleEvens2 orElse tripleOdds2 andThen addFive

whatToDo3(3)

// Implicits

// https://ds12.github.io/scala-class/slides/lecture10a.html#slide15
// Magma -> binary operater
// Semigroup -> associative binary operator
// Monoid -> with identity
// Group -> Inverse

abstract class SemiGroup[A] {
  def add(x: A, y: A): A
}

abstract class Monoid[A] extends SemiGroup[A]  {
  def unit: A
}

implicit object StringMonoid extends Monoid[String] {
  def add(x: String, y: String): String = x concat y
  def unit: String = ""
}

implicit object IntMonoid extends Monoid[Int] {
  def add(x: Int, y: Int): Int = x + y
  def unit: Int = 0
}

StringMonoid.add("test", "test")

def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
  if (xs.isEmpty) m.unit
  else m.add(xs.head, sum(xs.tail))

sum(List(1,2,3))
sum(List("a", "b", "c"))

class IntWrapper(value: Int)  {
  def isOdd = value % 2 != 0
}

implicit def IntExt(value: Int) = new IntWrapper(value)

15.isOdd

// defining an implicit method to extend existing Int class

object MyPredef {
  class KoanIntWrapper(val original: Int) {
    def isOdd = original % 2 != 0

    def isEven = !isOdd
  }
  implicit def thisMethodNameIsIrrelevant(value: Int) =
    new KoanIntWrapper(value)
}

import MyPredef._

20.isEven

import java.math.BigInteger

implicit def Int2BigIntegerConvert(value: Int): BigInteger =
  new BigInteger(value.toString)

def add(a: BigInteger, b: BigInteger) = a.add(b)

add(Int2BigIntegerConvert(3), Int2BigIntegerConvert(6))

def howMuchCanIMake_?(hours: Int)(implicit dollarsPerHour: BigDecimal) =
  dollarsPerHour * hours

implicit val hourlyRate = BigDecimal(34)

howMuchCanIMake_?(30)

// Traits

trait Similarity{
  def isSimilar(x: Any): Boolean
  def isNotSimilar(x: Any): Boolean = !isSimilar(x)
}

case class Event(name: String)

trait EventListener {
  def listen(event: Event): String
}


trait B {
  def bId = 2
}

trait A { self: B =>
  def aId = 1
}

val obj = new A with B
obj.aId
obj.bId


// For expressions

val xValues = 1 to 4
val yValues = 1 to 2

val coordinates = for {
  x <- xValues
  y <- yValues
} yield (x,y)


coordinates(4)

val nums = List(List(1), List(2), List(3), List(4), List(5))

val result = for {
  numList <- nums
  num <- numList
  if (num % 2 == 0)
} yield (num)

nums.flatMap(numList ⇒ numList).filter(_ % 2 == 0)

// Prefix and Postfix operators
val g: String = "Check out the big brains on Brad!"

g indexOf 'o'

g.indexOf('o', 7)

val g2: Int = 31
(g2 toHexString)


class Stereo {
  def unary_+ = "on"

  def unary_- = "off"
}

val stereo = new Stereo
(+stereo)
(-stereo)

// Scala supports the notion of case classes.
// Case classes are regular classes which export
// their constructor parameters and which provide
// a recursive decomposition mechanism via pattern matching.
// have a to string method

// untyped lambda calculus
abstract class Term
case class Var(name: String) extends Term
case class Fun(arg: String, body: Term) extends Term
case class App(f: Term, v: Term) extends Term


case class Person(first: String, last: String)

val p1 = new Person("Fred", "Jones")
val p2 = new Person("Shaggy", "Rogers")
val p3 = new Person("Fred", "Jones")

p1 == p2
p1 eq p2
p1 == p3
p1 eq p3

p1.toString()

case class Dog(name: String, breed: String)
val d1 = Dog("Scooby", "Doberman")
d1.toString

case class Person2(first: String, last: String, age: Int = 0, ssn: String = "")
val person = Person2("Fred", "Jones", 23, "111-22-3333")
val parts = Person2.unapply(person).get

//Case classes are Serializable:


// Repeated parameters
def repeatedParameterMethod(x: Int, y: String, z: Any*) = {
  "%d %ss can give you %s".format(x, y, z.mkString(", "))
}

repeatedParameterMethod(3, "egg", "a delicious sandwich", "protein", "high cholesterol")
repeatedParameterMethod(
  3,
  "egg",
  List("a delicious sandwich", "protein", "high cholesterol"): _*
)


// Variances

class Foo[+A] // A covariant class
class Bar[-A] // A contravariant class
class Baz[A]  // An invariant class

//abstract class Animal {
//  def name: String
//}
//case class Cat(name: String) extends Animal
//case class Dog(name: String) extends Animal

val z = "Do" :: "Re" :: "Mi" :: "Fa" :: "So" :: "La" :: "Te" :: "Do" :: Nil //Infers that the list assigned to variable is of type List[String]

// type classes
import simulacrum._

@typeclass trait ShowSim[A] {
  def showSim(a: A): String
}

object ShowSim {
  implicit val stringCanShow: ShowSim[String] =
    str => s"simulacrum string $str"
}
























