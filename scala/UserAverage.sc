import scala.io.Source

object UserAverage {
  case class UserRecord(name: String, game: String, score: Int)
  case class UserAverage(name: String, average: Double) {
    def print(): Unit = {
      println(f"${name}:${average}")
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

    def getAverage(name: String, records: List[UserRecord]): UserAverage = {
      val total = records.map{r => (r.score, 1)}.foldLeft((0,0)){case (acc, n) => (acc._1 + n._1, acc._2 + n._2)}
      val average = total._1.toDouble / total._2.toDouble
      UserAverage(name, average)
    }
  }

  def run(path: String) = {
    val source = Source.fromFile(path)
    source
      .getLines().drop(1) // skip header
      .toList
      .flatMap(UserRecord.fromString)
      .groupBy(_.name)
      .map(r => UserRecord.getAverage(r._1, r._2))
      .map(_.print())
  }
}

UserAverage.run("/Users/kyle/dev/excercises/data/users.txt")