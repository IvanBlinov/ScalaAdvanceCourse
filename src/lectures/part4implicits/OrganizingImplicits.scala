package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  //implicit def reverseOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _) WON'T WORK
  //implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1,4,5,2,3).sorted)
  //scala.Predef

  /*
    Implicits:
    - val/var
    - objects
    - accessor methods = defs with no parentheses
   */
  object Person {
    implicit def personOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => {
      if (p1.name.compareTo(p2.name) != 0)
        p1.name.compareTo(p2.name) < 0
      else p1.age < p2.age
    })
  }
  /*
  object SomeObject {
    implicit def personOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => {
      if (p1.name.compareTo(p2.name) != 0)
        p1.name.compareTo(p2.name) < 0
      else p1.age < p2.age
    }) WON'T WORK
  }
   */
  case class Person(name: String, age: Int)
  val persons = List (
    Person("John", 66),
    Person("Steve", 21),
    Person("Amy", 21)
  )
  println(persons.sorted)

  /*
    Implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types involved in the method signature

   */

  object AlphabeticOrdering {
    implicit def alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.name.compareTo(p2.name) < 0)
  }

  object AgeOrdering {
    implicit def ageOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.age < p2.age)
  }

  import AgeOrdering.ageOrdering
  println(persons.sorted)

  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }
  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

}
