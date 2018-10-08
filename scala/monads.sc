
trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
  def apply(a:A, b:A) = op(a,b)
}

val stringMonoid = new Monoid[String] {
  def op(a: String, b: String):String = a + b
  def zero: String =  ""
}

def listMonoid[A] = new Monoid[List[A]] {
  def op(a: List[A], b: List[A]): List[A] = a ++ b
  val zero: List[A] = Nil
}

stringMonoid.op("test", "test2")

listMonoid[Int].op(List(1,2,3), List(3,4,5))

val intAddition = new Monoid[Int] {
  def op(a: Int, b: Int): Int = a + b
  val zero = 0
}

intAddition.op(1,2)

val intMultiplication = new Monoid[Int] {
  def op(a: Int, b: Int): Int = a * b
  val zero = 1
}

intMultiplication(3,4)

val orBoolean = new Monoid[Boolean] {
  def op(a: Boolean, b: Boolean) = a || b
  val zero = true
}

orBoolean(true, true)
orBoolean(false, true)
orBoolean(true, false)
orBoolean(false, false)


val andBoolean = new Monoid[Boolean] {
  def op(a: Boolean, b: Boolean) = a && b
  val zero = false
}

andBoolean(true, true)
andBoolean(true, false)
andBoolean(false, true)
andBoolean(false, false)

//None, Some(a)
//None, Some(b)

def optionMonoid[A] = new Monoid[Option[A]] {
  def op(a: Option[A], b: Option[A]) = a orElse b
  val zero = None
}

optionMonoid(Some("tree"), None)
optionMonoid(None, Some("tree"))
optionMonoid(Some("tree1"), Some("tree2"))
optionMonoid(None, None)


// f(a) : A => A = a + 4
// g(b) : A => A = b -20
//Int => Int
//a + 4
//Int => Int
//a - 20
// (a + 4) - 20 composition
// f: A => A

def endoMonoid[A]: Monoid[A => A] = new Monoid[A => A] {
  def op(f: A => A, g: A => A): A => A = f.compose(g)
  val zero = identity
}

def compose = endoMonoid[Int].op(_ + 3, _ - 20)
compose.apply(20)

val words = List("a", "b", "c", "d")
words.foldLeft(stringMonoid.zero)(stringMonoid.op)
words.foldLeft("")(_ concat _)

// balanced fold allows for parallelism
// op(a, op(b, op(c, d))) right
// op(op(op(a,b), c), d) left

// balanced
// op(op(a,b), op(c,d))

sealed trait WC
case class Stub(chars: String) extends WC
case class Part(lstub: String, words: Int, rstub: String) extends WC

var wcMonoid = new Monoid[WC] {

  def op(a1: WC, a2: WC): WC = (a1, a2) match {
    case (a: Part, b: Part) => Part(a.lstub, a.words + (if ((a.rstub + b.lstub).isEmpty) 0 else 1) + b.words, b.rstub)
    case (a: Part, b: Stub) => Part(a.lstub, a.words, a.rstub + b.chars)
    case (a: Stub, b: Part) => Part(a.chars + b.lstub, b.words, b.rstub)
    case (a: Stub, b: Stub) => Stub(a.chars + b.chars)
  }
  val zero =  Stub("")
}

def countWords(str: String): Int = {
  val wc = foldMapV(str.toIndexedSeq, wcMonoid)(c =>
    if (c == ' ' || c == '.') Part("", 0, "")
    else Stub(c.toString)
  )

  wc match {
    case Part(l,w,r) => w + (if (l.isEmpty) 0 else 1) + (if (r.isEmpty) 0 else 1)
    case Stub(c) => if (c.isEmpty) 0 else 1
  }
}

def foldMapV[A,B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): B =
  if (v.length == 0) m.zero
  else if (v.length == 1) f(v(0))
  else {
    val s = v.splitAt(v.length / 2)
    m.op(foldMapV(s._1, m)(f), foldMapV(s._2, m)(f))
  }

countWords("test string this is it " * 1000000)
