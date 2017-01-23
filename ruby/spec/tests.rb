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

# file_modes.rb
describe File do

  before(:each) {
    file = File.open("./spec/data/read_only.txt", "w+")
    file.print("# Read Only File")
    file.close

    file = File.open("./spec/data/read_file.txt", "w+")
    file.print("# Read This File")
    file.close
  }  

  after(:each) {
    File.delete("./spec/data/read_only.txt") rescue nil
    File.delete("./spec/data/write_only.txt") rescue nil
    File.delete("./spec/data/append_only.txt") rescue nil
    File.delete("./spec/data/read_file.txt") rescue nil
    File.delete("./spec/data/write_file.txt") rescue nil
    File.delete("./spec/data/append_file.txt") rescue nil
  }

  it "r mode" do
    file = File.open("./spec/data/read_only.txt", "r")
    expect{ file.puts("test") }.to raise_error(IOError)
    expect(file.read).to eq("# Read Only File")
    file.close
  end

  it "w mode" do
    file = File.open("./spec/data/write_only.txt", "w")
    file.print("# Write Only File")
    expect{ file.read }.to raise_error(IOError)
    file.close

    file = File.open("./spec/data/write_only.txt", "r")
    expect(file.read).to eq("# Write Only File")
  end

  it "a mode" do
    file = File.open("./spec/data/append_only.txt", "w")
    file.print("# Append To File")
    file.close

    file = File.open("./spec/data/append_only.txt", "a")
    file.print("# Appended")
    expect{ file.read }.to raise_error(IOError)
    file.close

    file = File.open("./spec/data/append_only.txt", "r")
    expect(file.read).to eq("# Append To File# Appended")
  end

  it "r+ mode" do
    file = File.open("./spec/data/read_file.txt", "r+")
    file.print("Testing")
    expect(file.read).to eq("This File")  # print has moved the cursor forward 9 chars
    file.close

    file = File.open("./spec/data/read_file.txt", "r+")
    expect(file.read).to eq("TestingThis File")
    file.close
  end

  it "w+ mode" do
    file = File.open("./spec/data/write_file.txt", "w+")
    file.print("# Write Into This File")
    expect(file.read).to eq("")  # at end of file
    file.close

    file = File.open("./spec/data/write_file.txt", "w+")
    file.print("# Writing Another Thing")
    file.close

    file = File.open("./spec/data/write_file.txt", "r")
    expect(file.read).to eq("# Writing Another Thing")
    file.close
  end

  it "w+ mode" do
    file = File.open("./spec/data/write_file.txt", "w+")
    file.print("# Write Into This File")
    expect(file.read).to eq("")  # at end of file
    file.close

    file = File.open("./spec/data/write_file.txt", "w+")
    file.print("# Writing Another Thing")
    file.close

    file = File.open("./spec/data/write_file.txt", "r")
    expect(file.read).to eq("# Writing Another Thing")
    file.close
  end

  it "w+ mode" do
    file = File.open("./spec/data/append_file.txt", "a+")
    file.puts("# Append Onto This File")
    expect(file.read).to eq("")  # at end of file
    file.close

    file = File.open("./spec/data/append_file.txt", "a+")
    file.puts("# Writing Another Thing")
    file.close

    file = File.open("./spec/data/append_file.txt", "r+")
    expect(file.read).to eq("# Append Onto This File\n# Writing Another Thing\n")
    file.close
  end
end

