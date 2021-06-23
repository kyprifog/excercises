case class User(name: String, id: Int)
case class DirectedGraph(nodes: List[User], edges: List[(Int, Int)])

class ShortestPath(graph: DirectedGraph) {
  // Develop efficient algorithm to find the shortest path between two users in a graph
  // Try not to use higher level library like graphX or pregel operations
  // Assume directed graph

  def getNeighbors(user: User): List[User] = {
    val neighborIds = graph.edges.filter(_._1 == user.id).map(_._2)
    graph.nodes.filter{n => neighborIds.contains(n.id)}
  }

  def run(user1: User, user2: User, maxLevels: Int = 7) = {

    val neighbors = getNeighbors(user1)
    val initial: List[(List[User], List[User])] = List((List(user1), neighbors) )
    (0 to maxLevels).foldLeft((initial, false)) { case(acc, n) =>
      val alreadyMatched = acc._2
      if (!alreadyMatched) {
        val next = acc._1.flatMap{e =>
          val neighbors = e._2
          if (neighbors.nonEmpty) {
            neighbors.map{n => (e._1 ++ List(n), getNeighbors(n))}
          } else { List(e) }
        }

        val matching = next.find(_._1.reverse.head.id == user2.id)

        if (matching.isDefined) { (List(matching.get), true)
        } else {
          (next, false)
        }
      } else { acc }
    }._1.flatMap{r => r._1}

  }

}

// Tom -> Sally -> Frank -> John
// ( [Tom], [Sally, Jess, Jimmy])
// [ ([Tom, Sally], [Frank, Timmy, Jax]), ([Tom, Jess], [Sarah]), ([Tom, Jimmy], [])]
// [ ([Tom, Sally,Frank], [John]), ([Tom, Sally, Timmy],[]), .... ]
// added shorted from Sally to John

val user1 = User("Tom",  1)
val user2 = User("Sally", 2)
val user3 = User("Jess", 3)
val user4 = User("Jimmy", 4)
val user5 = User("Jax", 5)
val user6 = User("Tom", 6)
val user7 = User("Jess", 7)
val user8 = User("Sarah", 8)
val user9 = User("Frank", 9)
val user10 = User("John", 10)
val user11 = User("Timmy", 11)
val allUsers = List(
  user1,
  user2,
  user3,
  user4,
  user5,
  user6,
  user7,
  user8,
  user9,
  user10,
  user11,
)
val edges = List(
  (1,2),
  (1,3),
  (1,4),
  (2,9),
  (2,10),
  (2,11),
  (2,5),
  (3,8),
  (9,10)
)

val graph = DirectedGraph(allUsers, edges)
val sp = new ShortestPath(graph)

assert(sp.run(user1, user10).map(_.id) == List(1,2,10))