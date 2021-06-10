
//case class BTreeNode(root: Int, left: Option[BTreeNode] = None, right: Option[BTreeNode] = None)

case class Graph(nodes: List[Int], edges: List[(Int, Int)])

object BTree {

  def splitPreOrder(pre: List[Int]): Graph = {


    pre.foldLeft((List[Int](), List[(Int, Int)](), )) { case (acc, n) =>
      val nodes = acc._1
      val edges = acc._2
      val last = acc._3

    }

  }

}


//val test = List(40,20,1,30,50)
//val btree = BTree.splitPreOrder(test)
//BTree.printTree(btree)
//
//val test2 = List(40,20,5,1,6,30,50,33,70)
//val btree2 = BTree.splitPreOrder(test2)
//BTree.printTree(btree2)
