package lectures.part2afp

object LazyEvaluation extends App {

  //val x: Int = throw new RuntimeException
  lazy val x: Int = throw new RuntimeException
  //println(x)

  lazy val y: Int = {
    println("Hello")
    42
  }

  println(y)
  println(y)

  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = {
    false
  }

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    Thread.sleep(1000)
    println("waiting")
    42
  }
  println(byNameMethod(retrieveMagicValue))

  //use lazy vals
  def byNameMethodRight(n: => Int): Int = {
    //CALL BY NEED
    lazy val t = n
    t + t + t + 1
  }
  println(byNameMethodRight(retrieveMagicValue))

  // filtering
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30)
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println(gt20lazy)
  gt20lazy.foreach(println)

  //for-comprehensions use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a + 1
  //---
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)
  //SAME
}
