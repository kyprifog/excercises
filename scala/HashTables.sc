
// Simple hashtable implementation
class HashTable[K,V](values: List[(K,V)]) {

  val length = values.length
  val table: Map[Int, List[(K,V)]] = {
    values
      .map{case(k,v) => (k.hashCode % (length - 1), (k,v))}
      .groupBy(_._1)
      .mapValues{r => r.map(_._2)}
  }

  def get(key: K) = {
    table(key.hashCode % (length - 1)).find(_._1 == key)
  }.map(_._2)

}

val values = List(
  ("Test", "Stuff"),
  ("Test2", "OtherStuff"),
  ("Test3", "ThingsAndStuff"),
)

val hashTable = new HashTable(values)
assert(hashTable.get("Test").get == "Stuff")
assert(hashTable.get("Test2").get == "OtherStuff")
assert(hashTable.get("Bad").isEmpty)
