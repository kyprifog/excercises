
// Implement linked list along with method to get value at index and delete an index in the list and return new list
case class LinkedList[T](head: Option[T], tail: Option[LinkedList[T]]) {

  def get(index: Int): Option[T] = {

    def getRecursive(current: LinkedList[T], i: Int): Option[T] = {
      if (index == i) {
        current.head
      } else {
        current.tail.flatMap(c =>
          getRecursive(c, i+1)
        )
      }
    }
    getRecursive(this, 0)
  }

  def delete(index: Int): LinkedList[T] = {
    def deleteRecursive(current: LinkedList[T], i: Int): Option[LinkedList[T]] = {
      if ((index-1) == i) {
        Some(LinkedList(current.head, current.tail.flatMap(_.tail)))
      } else {
        current.tail.flatMap{c => deleteRecursive(c, i+1)}
      }
    }
    deleteRecursive(this, 0).getOrElse(this)
  }

  // Write code to remove duplicates from an unsorted linked list.

  //Implement an algorithm to find the kth to last element of a singly linked list.
  
  // Write code to partition a linked list around a value x, such that all nodes less than x come before all nodes greater than or equal to x.


}

// 1 -> 2 -> 3
val ll =  LinkedList(Some(1), Some(LinkedList(Some(2), Some(LinkedList(Some(3), None)))))
assert(ll.get(1).contains(2))
// 1 -> 3
assert(ll.delete(1) == LinkedList(Some(1), Some(LinkedList(Some(3), None))))
