
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.functions._

object OrderAnalysis {
  case class Order(order_id: Long, product_id: Long, quantity_sold: Double, sale_date: java.sql.Timestamp, store: String)
  case class Product(product_name: String, product_id: Long, store: String, price: Double)
  case class Total(store: String, total: Double) {
    def asString(): String = f"${store} - ${total}"
  }

  def run(orderPath: String, productPath: String) = {
    val spark = SparkSession
      .builder()
      .master("local[4]")
      .appName("OrderAnalysis")
      .getOrCreate()

    import spark.implicits._

    val orderSchema = Encoders.product[Order].schema
    val orders = spark.read
      .schema(orderSchema)
      .option("header", true)
      .option("ignoreLeadingWhiteSpace",true)
      .option("ignoreTrailingWhiteSpace",true)
      .option("timestampFormat", "MM-dd-yyyy'T'HH:mm:ss.SSSS")
      .csv(orderPath)
      .as[Order]
      .dropDuplicates("order_id")
      .withColumn("key", concat(col("product_id"), lit('-'), col("store")))
    orders.show()

    val productSchema = Encoders.product[Product].schema
    val products = spark.read
      .schema(productSchema)
      .option("header", true)
      .csv(productPath)
      .as[Product]
      .withColumn("key", concat(col("product_id"), lit('-'), col("store")))
      .select("price", "key")
      .dropDuplicates("key")

     products.show()

    val totals = products.join(orders, "key")
      .withColumn("total", col("quantity_sold") * col("price"))
      .groupBy("store")
      .agg(sum("total").as("total"))
      .as[Total]
      .sort(-col("total"))

    totals.show()
    totals.head()

  }

  def runSQL(orderPath: String, productPath: String): Total = {

    val spark = SparkSession
      .builder()
      .master("local[4]")
      .appName("OrderAnalysis")
      .getOrCreate()

    import spark.implicits._

    val orderSchema = Encoders.product[Order].schema
    val orders = spark.read
      .schema(orderSchema)
      .option("header", true)
      .option("ignoreLeadingWhiteSpace",true)
      .option("ignoreTrailingWhiteSpace",true)
      .option("timestampFormat", "MM-dd-yyyy'T'HH:mm:ss.SSSS")
      .csv(orderPath)
      .as[Order]
      .dropDuplicates("order_id")
      .withColumn("key", concat(col("product_id"), lit('-'), col("store")))

    val productSchema = Encoders.product[Product].schema
    val products = spark.read
      .schema(productSchema)
      .option("header", true)
      .csv(productPath)
      .as[Product]
      .withColumn("key", concat(col("product_id"), lit('-'), col("store")))
      .select("price", "key")
      .dropDuplicates("key")

    orders.createTempView("orders")
    products.createTempView("products")

    val query =
      """
      SELECT store, total FROM (
        SELECT
          store,
          SUM(total) AS total
        FROM (
          SELECT
            products.key AS key,
            store,
            quantity_sold,
            price,
            quantity_sold * price AS total
          FROM
            products JOIN orders ON products.key = orders.key
        ) GROUP BY store
      )
      """.stripMargin
    val totals = spark.sql(query).as[Total].collect().sortBy(-_.total)
    totals.take(1).head
  }


}

val path = "/Users/kyle/dev/excercises/data/"
val max = OrderAnalysis.run(path + "orders.txt", path + "products.txt")
assert(max.asString() == "walmart - 31400.0")

val maxSQL = OrderAnalysis.runSQL(path + "orders.txt", path + "products.txt")
assert(maxSQL.asString() == "walmart - 31400.0")
