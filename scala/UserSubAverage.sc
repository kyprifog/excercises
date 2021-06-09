import scala.io.Source

object UserSubAverage {
  case class UserRecord(name: String, game: String, score: Int)

  case class UserGameAverage(name: String, game: String, average: Double) {
    def print(): Unit = {
      println(f"${game}:${average}")
    }
  }

  object UserRecord {
    def fromString(s: String): Option[UserRecord] = {
      val splitted = s.split(",")
      try {
        Some(UserRecord(splitted(0), splitted(1), splitted(2).toInt))
      } catch {
        case e: Exception => {
          println(f"Bad Record: ${s}. ${e.toString}")
          None
        }
      }
    }

    def getGameAverage(name: String, game: String, records: List[UserRecord]): UserGameAverage = {
      val total = records.map{r => (r.score, 1)}.foldLeft((0,0)){case (acc, n) => (acc._1 + n._1, acc._2 + n._2)}
      val average = total._1.toDouble / total._2.toDouble
      UserGameAverage(name, game, average)
    }
  }

  def run(path: String) = {
    val source = Source.fromFile(path)
    source
      .getLines().drop(1) // skip header
      .toList
      .flatMap(UserRecord.fromString)
      .groupBy(r => (r.name, r.game))
      .map(r => UserRecord.getGameAverage(r._1._1, r._1._2, r._2)) // r._1._1 = name r._1._2 = game, r._2 = records
      .toList.sortBy(r => r.name)
      .groupBy(_.name)
      .map(r => {
        println(f"-" * 40)
        println(f"User: ${r._1}")
        println(f"-" * 40)
        r._2.map(_.print())
      })
  }

}

UserSubAverage.run("/Users/kyle/dev/excercises/data/users.txt")