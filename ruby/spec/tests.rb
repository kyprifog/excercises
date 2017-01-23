require 'bundler/setup' 
Bundler.require

Dir["./lib/*.rb"].each {|file| require file }

def read_data(name)
  File.open("./spec/data/#{name}").read
end

#bottles_of_beer.rb
describe BottlesOfBeer do
  let(:answer) { read_data("bottles_of_beer.txt") }
  it "should be correct" do
    expect(BottlesOfBeer.sing).to eq(answer)
  end
end

#nth.rb
describe Integer do
  let(:answer) { read_data("nth.txt") }

  it "ordinalize should be correct" do
    guess = [(0..25),(250..265),(1000..1025)].map{|n| n.map(&:ordinalize).join(", ")}.join(", ")
    expect(guess).to eq(answer)
  end
end

#topswops.rb
describe Topswops do
  let(:answer) { read_data("topswop.txt") }
  let(:answer_2) { read_data("max_topswops.txt") }

  it "swops should be correct" do
    array = [8, 3, 10, 2, 5, 9, 1, 7, 4, 6]
    expect(array.print_topswops).to eq(answer)
    expect(3.max_topswops).to eq(2)
  end

  it "max topswops should be correct" do
    expect(Topswops.print_maximums(5)).to eq(answer_2)
  end
end

# cycle_detection.rb
describe CycleDetection do
  let(:f) { lambda{ |x| ( x * x + 1 ) % 255 } }
  let(:answer) { read_data("cycle_detection.txt") }

  it "finds the right cycle" do
    expect(CycleDetection.brent(f, 3)).to eq([6,2])
  end

  it "prints the cycle information" do
    expect(CycleDetection.analyze(f, 3)).to eq(answer)
  end
end


# truncatable_primes.rb
describe TruncatablePrime do

  it "can find largest left truncatable below a large number" do
    expect(TruncatablePrime.find_max(1000000)).to eq(999907)
  end

  it "can find largest right truncatable below a large number" do
    expect(TruncatablePrime.find_max(10000, "right")).to eq(7393)
  end

end

# set_consolidation.rb
describe Set do

  let(:sets) {[
    Set.new(["H","I","K"]),
    Set.new(["A","B"]),
    Set.new(["C","D"]),
    Set.new(["D","B"]),
    Set.new(["F","G","H"])
  ]}

  let(:answer) {[
    Set.new(["A","B","C","D"]),
    Set.new(["G","F","I","H", "K"])
  ]}

  it "works" do
    expect(Set.consolidate(sets)).to eq(answer)
  end

end


# case_combinations.rb
describe String do 

  let(:answer) { read_data("case_combinations.txt").split("\n") }  

  it "works" do
    expect("tEst".case_combine).to match_array(answer)
  end
end


