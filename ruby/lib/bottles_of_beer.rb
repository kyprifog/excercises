# http://rosettacode.org/wiki/99_Bottles_of_Beer
module BottlesOfBeer
  def self.sing(max=99)
    answer = ""
    (1..max).to_a.reverse.each do |number|
      answer += "#{number} #{bob(number)} on the wall\n"
      answer += "#{number} #{bob(number)}\n"
      answer += "Take one down pass it around #{number - 1} #{bob(number-1)} on the wall\n"
    end
    answer
  end
  
  def self.bob(number)
    number == 1 ? "bottle of beer" : "bottles of beer"
  end
end