
//  Living  People:  Given  a  list  of  people  with  their  birth  and  death  years,
//  implement  a  method  to  compute the year with the most number of people alive.
//  You  may assume that all people were born between  1900 and 2000 (inclusive).
//  If   a person was alive during any portion of that year, they should be included  in that year's count.
//  For example. Person (birth = 1908, death = 1909) is   included in the counts for both  1908 and  1909.

object LivingPeople {
  // Assumption, just removing A year with the max
  def run(people: List[(Int, Int)]): (Int,Int) = {
    people
      .flatMap{p => (p._1 to p._2).toList}
      .map((_,1)).groupBy(_._1)
      .mapValues(_.length).toList
      .maxBy(_._2)
  }
}

val people = List(
  (1900, 1980),
  (1903, 1975),
  (1940, 1980),
  (1930, 2000),
  (1943, 2001),
  (1960, 2021),
  (1986, 2040)
)

assert(LivingPeople.run(people) == (1971, 6))


