class BalancedString(balanceChars: List[(Char, Char)])  {

  def check(s: String) = {
    val charList = balanceChars.flatMap(c => List(c._1, c._2))
    val relevantChars = s.toList.filter(charList.contains(_))
    relevantChars.foldLeft(""){ case (acc, n) =>
     if ((acc != "") && (balanceChars.find(_._2 == n).map(_._1).contains(acc.last))) {
       acc.dropRight(1)
     } else { acc + n }
    }.toList.isEmpty
  }
}

val balanceChars = List(
  ('(', ')'),
  ('[', ']'),
  ('<', '>')
)

val bs = new BalancedString(balanceChars)
assert(!bs.check("(()"))
assert(bs.check("(())"))
assert(bs.check("<<><><><<>>>"))
assert(!bs.check("<<><><><<>>>>"))
assert(!bs.check("<<><><><<>>>[[]][]]"))
assert(bs.check("<t>HERE IS SOME STUFF ((a),(b))</t>"))
