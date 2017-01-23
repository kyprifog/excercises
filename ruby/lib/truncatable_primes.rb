# http://rosettacode.org/wiki/Truncatable_primes
require 'prime'

module TruncatablePrime

  def self.find_max(maximum, direction="left", chunk_size = 10000)
    max = nil
    Array(1..maximum).reverse.each_slice(chunk_size) do |slice|
      slice.select{|x| x.prime?}.each do |prime|
        is_still_prime = true
        value = prime.to_s

        while value != ""
          is_still_prime = value.to_i.prime?
          break unless is_still_prime
          value = direction == "left" ? value.reverse.chop.reverse : value.chop
        end

        break (max = prime) if is_still_prime 

      end
      break unless max.nil?
    end
    return max
  end

end