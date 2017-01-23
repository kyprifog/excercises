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