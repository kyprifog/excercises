class String

  def case_combine
    cases = self.split("").map{|char| [char.downcase, char.upcase] }
    cases.inject([]) do |memo, value| 
      memo << (memo.last ? memo.pop.product(value) : value)
    end.flatten.each_slice(self.length).map(&:join)
  end

end