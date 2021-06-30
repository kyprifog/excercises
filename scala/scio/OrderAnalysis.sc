import com.spotify.scio.ScioContext

import scala.util.Try

object Util {
  case class Order(order_id: Long, product_id: Long, quantity_sold: Double, sale_date: String, store: String)
  case class Product(product_name: String, product_id: Long, store: String, price: Double)
  def extractOrder(s: String): Option[Order] = {
    Try{
      val splitted = s.split(",").map(_.trim).toList
      val order_id = splitted(0).toLong
      val product_id = splitted(1).toLong
      val quantity_sold = splitted(2).toLong
      val saleDate = splitted(3)
      val store = splitted(4)
      Order(order_id, product_id, quantity_sold, saleDate, store)
    }.toOption
  }

  def extractProduct(s: String): Option[Product] = {
    Try{
      val splitted = s.split(",").map(_.trim).toList
      val product_name = splitted(0)
      val product_id = splitted(1).toLong
      val store = splitted(2)
      val price = splitted(3).toDouble
      Product(product_name, product_id, store, price)
    }.toOption
  }
}

object OrderAnalysis {
  def run(orderPath: String, productPath: String) = {
    val sc = ScioContext()
    import Util._

    val orders = sc.textFile(orderPath).flatMap(extractOrder).distinctBy(_.order_id).map{o => ((o.product_id, o.store), o)}
    val products = sc.textFile(productPath).flatMap(extractProduct).map{p => ((p.product_id, p.store), p)}.distinctByKey

    orders.leftOuterJoin(products)
      .flatMap{j =>
        val product = j._2._2
        val order = j._2._1
        product.map{p =>
          (order.store, p.price * order.quantity_sold)
        }}
      .groupByKey.mapValues{t => t.sum}
      .tap(i => println(s"${i._1} : ${i._2}"))

    val result = sc.run().waitUntilFinish()
    result
  }

  def runMapSide(orderPath: String, productPath: String) = {
    val sc = ScioContext()
    import Util._

    val orders = sc
      .textFile(orderPath)
      .flatMap(extractOrder)
      .distinctBy(_.order_id)
      .map{o => ((o.product_id, o.store), o)}

    val products = sc.textFile(productPath)
      .flatMap(extractProduct)
      .map{p => ((p.product_id, p.store), p)}
      .distinctByKey
      .asMapSideInput


    orders
      .withSideInputs(products)
      .flatMap{(kv, side) =>
        val m = side(products)
        val key = kv._1
        val order = kv._2
        val product = m.get(key)
        product.map{p =>
          (order.store, p.price * order.quantity_sold)
        }}
      .toSCollection
      .groupByKey.mapValues{t => t.sum}
      .tap(i => println(s"${i._1} : ${i._2}"))

    val result = sc.run().waitUntilFinish()
    result
  }

  def runHash(orderPath: String, productPath: String) = {
    val sc = ScioContext()
    import Util._

    val orders = sc
      .textFile(orderPath)
      .flatMap(extractOrder)
      .distinctBy(_.order_id)
      .map{o => ((o.product_id, o.store), o)}

    val products = sc.textFile(productPath)
      .flatMap(extractProduct)
      .map{p => ((p.product_id, p.store), p)}
      .distinctByKey


    orders.hashLeftOuterJoin(products)
      .flatMap{j =>
        val product = j._2._2
        val order = j._2._1
        product.map{p =>
          (order.store, p.price * order.quantity_sold)
        }}
      .groupByKey.mapValues{t => t.sum}
      .tap(i => println(s"${i._1} : ${i._2}"))

    val result = sc.run().waitUntilFinish()
    result
  }

}

val path = "/Users/kyle/dev/excercises/data/"
OrderAnalysis.run(path + "orders.txt", path + "products.txt")
OrderAnalysis.runMapSide(path + "orders.txt", path + "products.txt")
OrderAnalysis.runHash(path + "orders.txt", path + "products.txt")

println("Done")








