//  Given a list of millions of documents, how would you find all documents that contain a list of words?
//  The words can appear in any order, but they must be complete words.
//  That is, "book" does not match "bookkeeper."

import faker.Lorem.paragraphs
import org.apache.spark.sql.{Dataset, SparkSession}

case class Document(id: Long, text: String)

object FindDocuments {

  def runSimple(documents: List[Document], words: List[String]): List[Document] = {
    documents
      .map { d =>
        val docWords = d.text.split("[^a-zA-Z']+").filter(_.nonEmpty).map(_.toLowerCase)
        (d, docWords.toSet.intersect(words.toSet) == words.toSet)
      }
      .filter(_._2 == true)
      .map(_._1)
  }

  def runSpark(spark: SparkSession, documents: Dataset[Document], words: List[String]): Dataset[Long] = {
    import spark.implicits._

    // Not strictly necessary, cuts down on overhead
    val broadcastWords = spark.sparkContext.broadcast(words.toSet)

    documents
      .map { d =>
        val docWords = d.text.split("[^a-zA-Z']+").filter(_.nonEmpty).map(_.toLowerCase)
        (d.id, docWords.toSet.intersect(broadcastWords.value) == broadcastWords.value)
      }
      .filter(_._2 == true)
      .map(_._1)
  }

  // attempt to leverage an inverted index
  //  def runSparkII(spark: SparkSession, documents: Dataset[Document], words: List[String]) = {
  //    import spark.implicits._
  //
  //    val broadcastWords = spark.sparkContext.broadcast(words.toSet)
  //
  //    documents.flatMap { d =>
  //        d.text
  //          .split("[^a-zA-Z']+")
  //          .filter(_.nonEmpty)
  //          .map(_.toLowerCase)
  //          .map(w => (w, List(d.id)))
  //      }
  //      .rdd
  //      .reduceByKey{case(l1, l2) => l1 ++ l2}
  //      .filter(r => broadcastWords.value.toList.contains(r._1))
  //      .map(_._2.toSet)
  //      .reduce(_.intersect(_))
  //      .toList
  //  }


  def generateDocuments(spark: SparkSession, numDocuments: Long, numParagraphs: Int, numFiles: Int, path: String) = {
    import spark.implicits._

    val documents = spark.range(numDocuments)
      .rdd.zipWithIndex
      .map{case(p, i) => Document(i, paragraphs(numParagraphs).mkString("\n"))}

    spark.createDataset(documents)
      .repartition(numFiles)
      .write.mode("overwrite")
      .parquet(path)
  }

}

val documents = List(
  Document(1, "this is a test document, it has alot of words"),
  Document(2, "this is another test document it has even more words"),
  Document(2, "this is the last test document")
)
val words = List("test", "document", "words")

//assert(FindDocuments.runSimple(documents, words).map(_.id) == List(1,2))

val spark = SparkSession
  .builder()
  .master("local[*]")
  .getOrCreate()
import spark.implicits._

//val documentsDataset = spark.createDataset[Document](documents)
//assert(FindDocuments.runSpark(spark, documentsDataset, words).collect().toList == List(1,2))

val docPath = "/Users/kyle/dev/data/documents/"
// This takes about 5-10 mins, generates 100 parquet files
// FindDocuments.generateDocuments(spark, 1000000L, 10, 100, docPath)

val loremWords = List(
  "dolores",
  "est",
  "sit",
  "ut",
  "minima",
  "sunt"
)

def time[R](block: => R): R = {
  val t0 = System.nanoTime()
  val result = block    // call-by-name
  val t1 = System.nanoTime()
  println("Elapsed time: " + (t1 - t0).toDouble/ 1000000000 + "s")
  result
}

val loremDocuments = spark.read.parquet(docPath).as[Document]

val numDocs = 168374

time {
  assert(FindDocuments.runSpark(spark, loremDocuments, loremWords).collect().toList.length == numDocs)
}

