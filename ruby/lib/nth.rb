# http://rosettacode.org/wiki/N%27th
class Integer
  def ordinalize
    return "Positive integer please" unless (self.to_i == self) && (self >= 0)
    digits = self.to_s[-2..-1]
    if ["11","12","13"].include?(digits)
      suffix = "th"
    else
      suffixes = {"1"=> "st", "2"=> "nd", "3"=> "rd"}
      suffix = suffixes[self.to_s[-1]] || "th"
    end
    [self.to_s, suffix].join("")
  end
end