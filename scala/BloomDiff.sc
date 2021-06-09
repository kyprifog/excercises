
import scala.io.Source

object BloomDiff {

  import java.math.BigInteger
  import java.security.MessageDigest

  class BloomFilter(
    length: Int,
    numHashes: Int,
    algorithm: String = "SHA1") {

    val digest = try {
      Some(MessageDigest.getInstance(algorithm))
    } catch {
      case e: Exception => None
    }

    val set = new Array[Byte](length)

    def hash(value: Int) : Int = digest.map(d => {
      d.reset
      d.update(value.byteValue())
      Math.abs(new BigInteger(1, d.digest).intValue) % (set.length -1)
    }).getOrElse(-1)

    def getSet(string: String): Array[Int] = {
      new Array[Int](numHashes)
        .foldLeft((hash(string.hashCode), Array[Int]())){
          case (acc, n) => (hash(acc._1), acc._2 ++ List(acc._1))
        }._2
    }

    def add(elements: Array[String]): Int = digest.map(_ => {
      elements.foreach(getSet(_).foreach(set(_) = 1) )
      elements.length
    }).getOrElse(-1)

    final def contains(s: String): Boolean = {
      digest.map( _ => !getSet(s).exists(set(_) !=1)).getOrElse(false)
    }

  }


  def run(file1: String, file2: String) = {
    val f1 = Source.fromFile(file1)
    val f2 = Source.fromFile(file2)
    val bf = new BloomFilter(64, 10)

    val col1 = f1.getLines.toList
      .map(_.trim.toLowerCase)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))

    val col2 = f2.getLines.toList
      .map(_.trim.toLowerCase)
      .flatMap(_.split("[^a-zA-Z']+").filter(_.nonEmpty))

    for (c <- col2) {
      bf.add(Array(c))
      println(s"${c}:${bf.contains("Prifogle")}")
    }

    // TODO: this isn't working yet
    val diff = col1.filterNot(r => bf.contains(r))

  }

}

val data = "/Users/kyle/dev/excercises/data/"
BloomDiff.run(data + "pg1787.txt", data + "kinglear.txt")

