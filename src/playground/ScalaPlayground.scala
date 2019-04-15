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
}
