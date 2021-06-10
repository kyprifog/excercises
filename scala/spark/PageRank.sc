import org.apache.spark.sql.{Encoders, SparkSession}

case class Link(from: String, to: String)
class PageRank(source: String, iters: Int = 10) {

  //TODO: Largely lifted from wiki, improve

  lazy val ranks = {

    val spark = SparkSession
      .builder
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val schema = Encoders.product[Link].schema
    val lines = spark.read.schema(schema).option("header", true).csv(source).as[Link]
    val links = lines.map{ s =>
      (s.from, s.to)
    }.distinct().rdd.groupByKey().cache()

    var workingRanks = links.mapValues(v => 1.0)

    for (i <- 1 to iters) {
      val contribs = links.join(workingRanks).values.flatMap{ case (urls, rank) =>
        val size = urls.size
        urls.map(url => (url, rank / size))
      }
      workingRanks = contribs.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)
    }

    val output = workingRanks.collect()
    val ranks = output.sortBy(_._1).toMap

    spark.stop()
    ranks

  }

  def rank(s: String) = {
    ranks.getOrElse(s, 0.0)
  }


}

val pr = new PageRank("/Users/kyle/dev/excercises/data/links.txt")
assert(pr.rank("test.com") == 0.6846921872053602)
assert(pr.rank("test2.com") == 0.38263236031433145)
assert(pr.rank("test3.com") == 0.26789499525362365)
assert(pr.rank("test4.com") == 0.43195542936962195)
assert(pr.rank("test5.com") == 0.6878498173982761)
assert(pr.rank("test6.com") == 0.5555084673747979)
