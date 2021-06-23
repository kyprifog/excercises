//* <br> Problem Statement :
//  * ASCII v/s UNICODE - String can be either of these. ASCII has basically 128
//  * characters (256 in a extended set) ranging from 0 - 127 whereas, UNICODE is
//  * a superset of ASCII and has 0 - 2^21 characters. Only difference is size.
//  *
//  * </br>

object String {

  //  *
  //  * Implement an algorithm to determine if a string has all unique characters.
  //  * What if you cannot use any additional data structure?

  def uniqueChar(s: String) = s.toList.length == s.toList.toSet.toList.length

  // * Given two strings, write a method to decide if one is the permutation of other.

  def isPerm(s1: String, s2: String) = s1.toArray.toList.sorted == s2.toArray.toList.sorted

  // Given a string, write a function to check if it is a permutation of a palin­drome.

  def isPalindromePerm(s: String): Boolean = {
    val list = s.toArray.toList
    val odd = list.groupBy{f => f}.mapValues(_.length % 2).values.filter(_!=0)
    (odd.toList.length == (list.length % 2))
  }

  // Write an algorithm such that if an element in an MxN matrix is 0,
  // its entire row and column are set to 0.


}

assert(!String.uniqueChar("TEST"))
assert(String.uniqueChar("ABC"))
assert(String.isPerm("ABC", "CAB"))
assert(!String.isPerm("TEST", "CAB"))

assert(!String.isPalindromePerm("AB"))
assert(String.isPalindromePerm("A"))
assert(String.isPalindromePerm(""))
assert(String.isPalindromePerm("AAA"))
assert(String.isPalindromePerm("ABA"))
assert(String.isPalindromePerm("AABAA"))
assert(String.isPalindromePerm("AABBAA"))
assert(String.isPalindromePerm("AABBBAA"))

