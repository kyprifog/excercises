# http://rosettacode.org/wiki/Topswops
module Topswops
  def self.print_maximums(range)
    Array(1..range).map do |value|
      [value,value.max_topswops].join(":")
    end.join("\n")
  end
end

class Array

  def topswops
    swop = self
    array = [self]
    while swop.first != 1
      i =(swop.first - 1)
      swop = swop[0..i].reverse + swop[i+1..-1]
      array << swop
    end
    array

  end

  def print_topswops
    topswops.map{|array| array.join(",")}.join("\n")
  end

end

class Integer

  def max_topswops
    # permutation is very slow for n > 9, find analytical answer
    Array(1..self).permutation.map{|a| a.topswops.length}.max - 1
  end

end