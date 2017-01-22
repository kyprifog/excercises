# http://rosettacode.org/wiki/Cycle_detection
module CycleDetection

  def self.analyze(f, x0)
    cycle_length, cycle_start = self.brent(f,x0)
    analysis = []
    sample = [x0]
    value = x0
    13.times.each {|time| sample << (value = f.call(value)) }
    analysis << "Sample = [#{sample.join(",")},...]"
    analysis << "Cycle length = #{cycle_length}"
    analysis << "Start index = #{cycle_start}"
    analysis << "Cycle = [#{sample[cycle_start..(cycle_start+cycle_length-1)].join(",")}]"
    return analysis.join("\n")
  end

  def self.brent(f, x0)
    power = lam = 1
    tortoise = x0
    hare = f.call(x0)
    while tortoise != hare
      if power == lam
        tortoise = hare
        power *= 2
        lam = 0
      end
      hare = f.call(hare)
      lam +=1
    end

    mu = 0
    tortoise = hare = x0
    lam.times { hare = f.call(hare) }

    while tortoise != hare
      tortoise = f.call(tortoise)
      hare = f.call(hare)
      mu += 1
    end

    return lam, mu
  end

end