package lectures.part5typesystem

object RockingInheritance extends App {

  //convenience
  trait Writer[T] {
    def write(value: T): Unit
  }
  trait Closable {
    def close(status: Int): Unit
  }
  trait GenericString[T] {
    //some methods
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream:GenericString[T] with Writer[T] with Closable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  //diamond problem
  trait Animal {def name: String}
  trait Lion extends Animal {
    override def name: String = "Lion"
  }
  trait Tiger extends Animal {
    override def name: String = "Tiger"
  }
  trait Mutant extends Lion with Tiger
  class MutantClass extends Lion with Tiger {
    override def name: String = "Mutant Class"
  }
  class EmptyMutant extends Lion with Tiger

  val m = new EmptyMutant
  println(m.name)
  // Always pick last inheritance

  //the SUPER PROBLEM + type linearization

  trait Cold {
    def print = println("cold")
  }
  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }
  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }
  trait Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print
}
