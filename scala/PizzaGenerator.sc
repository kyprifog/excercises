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

    def print(i: Integer): Unit = {
      val message = if (i == 1) {
        s"${i}. Vegetarian: ${hash()}"
      } else {
        s"${i}. ${hash()}"
      }
      println(message)
    }
  }

  case class Menu(pizzas: List[Pizza]) {
    def print(today: LocalDateTime): Unit =  {
      println("-"*100)
      val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
      println(s"Today's Pizza Menu (${today.toLocalDate.format(formatter)}):")
      println("-"*100)
      for ((p,i) <- pizzas.zipWithIndex) { p.print(i+1) }
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
        val pizza = getPizza(prevHistory.map(_._2), n)
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

  def getPizza(history: List[String], n: Integer): Option[Pizza] = {


    var pizza: Option[Pizza] = None
    // Sanity check, sum of combinations is 2 ^ length
    val maxCombinations =  scala.math.pow(2,maxLength).toInt

    for (i <-(0 to maxCombinations) if (pizza.isEmpty)) {
      val candidatePizza = generatePizza(maxLength, n)

      // this could be improved with a bloom filter
      if (!history.contains(candidatePizza.hash())) {
        pizza = Some(candidatePizza)
      }
    }
    pizza
  }

  def generatePizza(maxLength: Int, index: Int): Pizza = {
    // This skews towards lower topping pizzas, could improve it by making number unevenly distributed

    val meatIngredients = Set(
      "pepperoni",
      "bacon",
      "ham"
    )

    val ingred = if (index == 1) {
      ingredients.diff(meatIngredients)
    } else { ingredients }

    val number = scala.util.Random.nextInt(maxLength)
    val entries = Random.shuffle(ingred.toList).take(number)
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
//    1. Vegetarian: artichoke, arugula, basil, garlic, green peppers, mushroom, olives, onion, pineapple
//    2. None
//    3. arugula, bacon, basil, ham, mushroom, olives, onion, pepperoni, pineapple
//    4. artichoke, arugula, bacon, basil, garlic, olives, onion, pepperoni
//    5. arugula, bacon, garlic, green peppers, mushroom, pineapple
//    6. artichoke, arugula, bacon, basil, green peppers, ham, mushroom, olives, onion, pepperoni
//    7. artichoke, arugula, basil, mushroom
//    8. arugula, bacon, basil, garlic, ham, mushroom, olives, pepperoni, pineapple
//    9. artichoke, arugula, green peppers, onion, pineapple
//    10. arugula, bacon, garlic, green peppers, mushroom, olives, pineapple
//    ----------------------------------------------------------------------------------------------------
//    Today's Pizza Menu (2020-07-10):
//    ----------------------------------------------------------------------------------------------------
//    1. Vegetarian: artichoke, arugula, garlic, mushroom, olives, onion, pineapple
//    2. garlic, onion, pepperoni
//    3. artichoke, arugula, bacon, basil, garlic, green peppers, ham, onion, pineapple
//    4. artichoke, arugula, green peppers, onion, pepperoni
//    5. artichoke, arugula, bacon, basil, garlic, green peppers, ham, mushroom, olives, onion, pineapple
//    6. artichoke, arugula, green peppers, ham, olives, onion, pepperoni, pineapple
//    7. bacon, basil, garlic, mushroom, onion, pepperoni, pineapple
//    8. basil, green peppers, ham, mushroom, olives, pepperoni, pineapple
//    9. bacon, green peppers, mushroom, pepperoni
//    10. arugula, basil, garlic, ham, pepperoni, pineapple
//    ----------------------------------------------------------------------------------------------------
//    Today's Pizza Menu (2021-06-17):
//    ----------------------------------------------------------------------------------------------------
//    1. Vegetarian: artichoke, arugula, basil, garlic, green peppers, mushroom, onion, pineapple
//    2. artichoke, arugula, bacon, olives, onion, pineapple
//    3. artichoke, arugula, bacon, basil, ham, onion, pineapple
//    4. basil, green peppers, mushroom, olives, onion, pepperoni
//    5. arugula, bacon, basil, garlic, green peppers, ham, mushroom, olives, onion, pepperoni, pineapple
//    6. artichoke, arugula, bacon, garlic, ham, mushroom, olives, onion, pepperoni, pineapple
//    7. artichoke, arugula, bacon, green peppers, ham, mushroom, olives, pineapple
//    8. arugula, bacon, basil, green peppers, ham, mushroom, olives, onion, pepperoni, pineapple
//    9. pineapple
//    10. onion



// Shows that you probbably wont get pizzas recommend from bitAgo, today
for (s <- (0 to 1000)) {
  val (menu0, history0) = pg.getTodaysMenu(numPizzas = 10, date=Some(whileAgo))
  val (menu1, history1) = pg.getTodaysMenu(numPizzas = 10, Some(history0), Some(bitAgo))
  val (menu2, history2) = pg.getTodaysMenu(numPizzas = 10, Some(history1), Some(today))
  assert(menu2.pizzas.toSet.intersect(menu1.pizzas.toSet).toList.isEmpty)
}

println("\nDone")

