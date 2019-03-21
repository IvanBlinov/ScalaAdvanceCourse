package lectures.part1advancescala

object AdvanceScalaMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))
    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n , a) => s"Hi, my name is $n and I'm $a years old"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg > - 10 && arg < 10) Some(true)
      else None
  }

  val n: Int = 45
  val mathProperty = n match {
    case singleDigit(_) => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

}
