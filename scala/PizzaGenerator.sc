import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Random

class PizzaGenerator(ingredients: Set[String], maxLength: Int) {
  // Assumptions:
  // - Runs on same timezone machine
  // - Rolling window for time
  // - Not handling history between instantiations.  Can assume saving history to local file system

  case class Pizza(toppings: Set[String]) {
    def hash(): String = {
      if (toppings.isEmpty) { "None" } else { s"${toppings.toList.sorted.mkString(", ")}"}
    }

    def print(): Unit = println(hash())
  }

  case class Menu(pizzas: List[Pizza]) {
    def print(today: LocalDateTime): Unit =  {
      println("-"*100)
      val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
      println(s"Today's Pizza Menu (${today.toLocalDate.format(formatter)}):")
      println("-"*100)
      for (p <- pizzas) { p.print() }
    }
  }

  case class History(entries: List[(LocalDateTime, String)])
  object History {
    val empty: History = History(List())
  }

  def getTodaysMenu(
    numPizzas: Int,
    history: Option[History] = None,
    date: Option[LocalDateTime] = None,
    printStdout: Boolean = false
  ): (Menu, History)  = {

    val today = date.getOrElse(LocalDateTime.now())
    val yearAgo = today.minusYears(1)
    val hist = history.getOrElse(History.empty)
    val lastYearsEntries = hist.entries.filter{e => (e._1.isAfter(yearAgo)) }

    val (pizzas, newEntries) = (1 to numPizzas).foldLeft((List[Pizza](), lastYearsEntries)){case (acc, n) =>
        val newPizzas: List[Pizza] = acc._1
        val prevHistory: List[(LocalDateTime, String)] = acc._2
        val pizza = getPizza(prevHistory.map(_._2))
        pizza.map{p =>
          (newPizzas ++ pizza, prevHistory ++ List((today, p.hash())))
        }.getOrElse(newPizzas, prevHistory)
    }
    val menu = Menu(pizzas)

    if (menu.pizzas.length == numPizzas) {
      if (printStdout) { menu.print(today) }
      (menu, History(newEntries))
    } else {
      println("Ran out of new pizza combinations! Increase toppings!")
      (Menu(List()), hist)
    }
  }

  def getPizza(history: List[String]): Option[Pizza] = {
    var pizza: Option[Pizza] = None
    // Sanity check, sum of combinations is 2 ^ length
    val maxCombinations =  2 ^ maxLength

    for (i <-(0 to maxCombinations) if (pizza.isEmpty)) {
      val candidatePizza = generatePizza(maxLength)

      // this could be improved with a bloom filter
      if (!history.contains(candidatePizza.hash())) {
        pizza = Some(candidatePizza)
      }
    }
    pizza
  }

  def generatePizza(maxLength: Int): Pizza = {
    // This skews towards lower topping pizzas, could improve it by making number unevenly distributed
    val number = scala.util.Random.nextInt(maxLength)
    val entries = Random.shuffle(ingredients.toList).take(number)
    Pizza(entries.toSet)
  }
}

/// TEST CODE

val toppings: Set[String] = List(
  "garlic",
  "mushroom",
  "pineapple",
  "pepperoni",
  "green peppers",
  "arugula",
  "bacon",
  "basil",
  "onion",
  "ham",
  "olives",
  "artichoke"
).toSet

val pg = new PizzaGenerator(toppings, 12)
val whileAgo = LocalDateTime.of(2019, 9, 30,0,0,0)
val bitAgo = LocalDateTime.of(2020, 7, 10,0,0,0)
val today = LocalDateTime.of(2021, 6, 17,0,0,0)

val (menu0, history0) = pg.getTodaysMenu(numPizzas = 10, date=Some(whileAgo), printStdout=true)
val (menu1, history1) = pg.getTodaysMenu(numPizzas = 10, Some(history0), Some(bitAgo), true)
val (menu2, history2) = pg.getTodaysMenu(numPizzas = 10, Some(history1), Some(today), true)

// -> STDOUT
//    ----------------------------------------------------------------------------------------------------
//    Today's Pizza Menu (2019-09-30):
//    ----------------------------------------------------------------------------------------------------
//    bacon, garlic, ham, mushroom, olives
//    artichoke, arugula, bacon, basil, ham, olives, pineapple
//    garlic
//    pepperoni
//    green peppers, olives
//    arugula, garlic, green peppers, ham, olives, onion, pepperoni
//    artichoke, arugula, basil, ham, olives, onion, pineapple
//    None
//    artichoke, bacon, basil, garlic, olives
//    arugula, bacon, garlic, ham, mushroom
//    ----------------------------------------------------------------------------------------------------
//    Today's Pizza Menu (2020-07-10):
//    ----------------------------------------------------------------------------------------------------
//    basil
//    ham, olives
//    bacon, basil, garlic, onion, pepperoni
//    artichoke, bacon, onion, pineapple
//    artichoke, arugula, bacon, basil, garlic, green peppers, ham, mushroom, olives, pepperoni, pineapple
//    ham, onion, pepperoni
//    artichoke, olives
//    bacon, garlic, green peppers, pepperoni
//    artichoke, bacon, garlic, green peppers, ham, onion, pepperoni
//    mushroom, olives, pineapple
//    ----------------------------------------------------------------------------------------------------
//    Today's Pizza Menu (2021-06-17):
//    ----------------------------------------------------------------------------------------------------
//    bacon, basil, green peppers, ham, mushroom, olives, onion, pineapple
//    None
//    basil, green peppers, ham, olives, pineapple
//    artichoke, arugula, bacon, garlic, green peppers, ham, mushroom, olives, pineapple
//    olives
//    arugula, bacon, basil, garlic, green peppers, onion, pepperoni, pineapple
//    green peppers, mushroom, pepperoni, pineapple
//    arugula, garlic, green peppers, olives
//    green peppers, olives
//    artichoke, arugula, bacon, garlic, olives, onion, pepperoni, pineapple

assert(menu2.pizzas.toSet.intersect(menu1.pizzas.toSet).toList.isEmpty)

// Shows that you probbably wont get pizzas recommend from bitAgo, today
for (s<-(0 to 1000)) {
  val (menu0, history0) = pg.getTodaysMenu(numPizzas = 10, date=Some(whileAgo))
  val (menu1, history1) = pg.getTodaysMenu(numPizzas = 10, Some(history0), Some(bitAgo))
  val (menu2, history2) = pg.getTodaysMenu(numPizzas = 10, Some(history1), Some(today))
  assert(menu2.pizzas.toSet.intersect(menu1.pizzas.toSet).toList.isEmpty)
}

println("\nDone")
