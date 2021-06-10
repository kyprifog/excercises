import scala.io.Source

class PageRank(path: String, iterations: Int = 10) {

  private def getEntry(s: String): Option[(String, String)] = {
    try {
      val splitted = s.split(",")
      Some((splitted(0), splitted(1)))
    } catch {
      case e: Exception => None
    }
  }

  val source = Source.fromFile(path)
  val links = source.getLines.toList.drop(1)
    .flatMap(getEntry)
    .distinct
    .groupBy(_._1)
    .map { r => (r._1, r._2.map { t => t._2 }) }

  val its = (1 to iterations).toList
  val initialRank = links.map { v => (v._1, 1.0) }
  lazy val ranks = its.foldLeft(initialRank) { case(acc, n) =>
    links
      .map { case (source, urls) =>
        val rank = acc.find { a => a._1 == source }
          .map { t => t._2 }
          .getOrElse(1.0)
        (urls, rank)
      }
      .toList.flatMap{ case(urls, rank) =>
        val size = urls.length.toDouble
        urls.map(u => (u, rank / size))}
      .groupBy{r => r._1}
      .map{r => (r._1,r._2.map{t => t._2}.sum)}
      .mapValues(0.15 + 0.85 * _)
  }

  def rank(link: String) = {
    ranks.getOrElse(link, 0.0)
  }
}

val pr = new PageRank("/Users/kyle/dev/excercises/data/links.txt")
assert(pr.rank("test.com") == 0.6846921872053602)
assert(pr.rank("test2.com") == 0.38263236031433145)
assert(pr.rank("test3.com") == 0.26789499525362365)
assert(pr.rank("test4.com") == 0.43195542936962195)
assert(pr.rank("test5.com") == 0.6878498173982761)
assert(pr.rank("test6.com") == 0.5555084673747979)




