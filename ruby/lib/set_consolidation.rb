# http://rosettacode.org/wiki/Set_consolidation
require 'set'

class Set

  def self.consolidate(sets)
    while sets.combination(2).any?{|set1,set2| 
      set1.merge(set2) && sets.delete(set2) if set1.intersect?(set2)} 
    end
    sets.reverse
  end

end
