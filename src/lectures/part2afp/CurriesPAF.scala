package lectures.part2afp

object CurriesPAF extends App {

  val superAdder: Int => Int => Int = {
    x => y => x + y
  }

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  //METHOD
  def curriedAdder(x: Int)(y: Int): Int = x + y //curried method

  val add4: Int => Int = curriedAdder(4)

  //lifting = ETA-EXPANSION

  //functions != methods
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) //ETA- expansion
  List(1,2,3).map(x => inc(x))

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // add7 : Int => Int = y => 7 + y
  val add7_1: Int => Int = x => simpleAddFunction(7, x)
  val add7_2 = (x: Int) => simpleAddFunction(7, x)
  val add7_2_1 = simpleAddFunction(7, _: Int)
  val add7_2_2 = simpleAddFunction.curried(7)
  val add7_3: Int => Int = x => simpleAddMethod(7, x)
  val add7_4: Int => Int = curriedAddMethod(7)
  val add7_5 = curriedAddMethod(7) _
  val add7_6 = curriedAddMethod(7)(_)

  val add7_8 = simpleAddMethod(7, _: Int) //alternative syntax for turning methods into functions

  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you")
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))


  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ //lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))


  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  byName(23) // ok
  byName(method) // ok
  byName(parenMethod()) // ok
  byName(parenMethod) // ok but beware ==> byName(parenMethod())
  // byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //byName(parenMethod _) // not ok

  //byFunction(45) // not ok
  //byFunction(method) // not ok
  byFunction(parenMethod) // ok ETA-expansion!
  byFunction(() => 46) // ok
  byFunction(parenMethod _) // unnecessary underscore
}
