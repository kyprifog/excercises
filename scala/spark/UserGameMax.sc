import org.apache.spark.sql.{Encoders, SparkSession}

object UserGameMax {
  case class Record(name: String, game: String, score: String)

  def run(source: String) = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    import spark.implicits._

    val schema = Encoders.product[Record].schema
    val records = spark.read.option("header", true).schema(schema).csv(source).as[Record]
    records.createTempView("records")

    val query =
      """
         SELECT
            name,
            game,
            max_score as score,
            RANK() OVER (PARTITION BY game ORDER by max_score) AS rank
         FROM (
            SELECT
              name,
              game,
              MAX(score) as max_score
            FROM records
            GROUP BY name, game
         )
      """.stripMargin

    val rankings = spark.sql(query)
    rankings.show()
  }
}

UserGameMax.run("/Users/kyle/dev/excercises/data/users.txt")

