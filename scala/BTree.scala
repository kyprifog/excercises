package kprifogle.excercises

import scala.collection.mutable.ListBuffer

case class BTreeNode(root: Int, left: Option[BTreeNode] = None, right: Option[BTreeNode] = None)

object BTree {

  def printTree(node: Option[BTreeNode]) = {
    val n = node.get
    var buffer = ListBuffer[Option[BTreeNode]]()
    var margin = 10
    println(" " * margin + n.root)
    buffer ++= List(n.left, n.right)
    while(buffer.length > 0) {
      margin -= 1
      println(" " * margin + buffer.map{r => r.map{s => s.root}.getOrElse("   ")}.mkString("   "))
      val b = buffer.flatten.map{r => List(r.left, r.right)}.flatten
      buffer = b
    }
  }

  def splitPreOrder(a: Array[Int]): Option[BTreeNode] = {
    if (a.length == 0) { None }
    else if (a.length == 1) { Some(BTreeNode(a(0))) }
    else {
      val rest = a.tail
      val splitVal = rest.find(_ > a(0)).getOrElse(0)
      val (left, right) = rest.splitAt(rest.indexOf(splitVal))
      Some(BTreeNode(a(0), splitPreOrder(left), splitPreOrder(right)))
    }
  }
}

object Main extends App {
  val test = Array(40,20,1,30,50)
  val btree = BTree.splitPreOrder(test)
  BTree.printTree(btree)

  val test2 = Array(40,20,5,1,6,30,50,33,70)
  val btree2 = BTree.splitPreOrder(test2)
  BTree.printTree(btree2)
}

