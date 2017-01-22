module InverseDist

  class Base

    def initialize(distribution, length)
      @distribution = get_distribution(distribution, length)
    end

    def print
      @distribution.shuffle.each do |item|
        puts item
      end
    end

    def get_distribution(percentage_distribution, length)
      array = []
      percentage_distribution.each do |k,v|
        array += ([k.to_s] * ((v) * length.to_f).floor)
      end
      array
    end
  end

  class QuarterInverseDist < Base
    def initialize(year, distribution, length)
      @year = year
      @distribution = distribution
      @length = length
    end

    def print
      quarters = quarter_ranges(@year)
      values = []
      quarters.each do |quarter_symbol,range|
        dist = @distribution[quarter_symbol]
        if dist
          quarter_distribution = get_distribution(dist, @length)
          quarter_distribution.each do |value|
            values << [range.to_a.sample, value]
          end
        end
      end
      sorted = values.sort_by {|i| i.first }
      sorted.each {|value| puts value.join(",") }
    end

    def quarter_ranges(year)
      {
        q1: ::Date.new(year,  1, 1)..::Date.new(year,  3, -1),
        q2: ::Date.new(year,  4, 1)..::Date.new(year,  6, -1),
        q3: ::Date.new(year,  7, 1)..::Date.new(year,  9, -1),
        q4: ::Date.new(year, 10, 1)..::Date.new(year, 12, -1)
      }
    end

  end

end
