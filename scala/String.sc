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
  //  *

  def uniqueChar(s: String) = s.toList.length == s.toList.toSet.toList.length

  // * Given two strings, write a method to decide if one is the permutation of other.

  def isPerm(s1: String, s2: String) = s1.toArray.toList.sorted == s2.toArray.toList.sorted


}

assert(!String.uniqueChar("TEST"))
assert(String.uniqueChar("ABC"))
assert(String.isPerm("ABC", "CAB"))
assert(!String.isPerm("TEST", "CAB"))

