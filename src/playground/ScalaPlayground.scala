package playground

object ScalaPlayground extends App {

  println("Hello, Scala")

  trait Hello[T] {
    def sayHello()
  }

  implicit class IntHello(val value: Int) extends Hello[Int] {
    override def sayHello(): Unit = println(s"int hello $value")
  }

  class StringImplicit extends Hello[String] {
    override def sayHello(): Unit = println(s"string hello")
  }

  new IntHello(42).sayHello()
  42.sayHello()

  class Coffee(val price: Double = 20)
  class CreditCard
  case class Charge(cc: CreditCard, amount: Double) {
    def combine(other: Charge): Charge = {
      if (cc == other.cc) {
        Charge(cc, amount + other.amount)
      } else {
        throw new Exception("Can't combine charges from other credit card")
      }
    }
  }

  class Cafe {
    def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
      val coffee = new Coffee
      (coffee, Charge(cc, coffee.price))
    }

    def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {
      val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
      val (coffees, charges) = purchases.unzip
      (coffees, charges.reduce((c1, c2) => c1.combine(c2)))
    }

    def coalesce(charges: List[Charge]): List[Charge] = {
      charges.groupBy(_.cc).values.map(_.reduce(_ combine _)).toList
    }
  }
}
