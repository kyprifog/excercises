import com.spotify.scio.ScioContext
import com.spotify.scio.parquet.types._


case class Document(id: Long, text: String)

object FindWords {
  def run(input: String): Unit = {
    val sc = ScioContext()
    val documents = sc.typedParquetFile[Document](input)
    documents.tap(println(_))

    val result = sc.run().waitUntilFinish()
    println(result.state.name())
  }
}

FindWords.run("/Users/kyle/dev/data/documents/part-00000-03db2a40-0e93-4564-8930-08120ed6c40e-c000.snappy.parquet")
