package lectures.part5typesystem

object StructuralTypes extends App {

  // structural types

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("Yeah, yeah, I'm closing")
    def closeSilently(): Unit = println("Nothing")
  }

  //def closeQuietly(closeable: JavaCloseable Or HipsterCloseable)
  type UnifiedCloseable = {
    def close(): Unit
  }//STRUCTURAL TYPE

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close

  closeQuietly(new JavaCloseable{
    override def close(): Unit = println("Java closeable")
  })

  closeQuietly(new HipsterCloseable)

  // TYPE REFINEMENTS

  type AdvanceCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaCloseable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advCloseable: AdvanceCloseable): Unit = advCloseable.closeSilently

  closeShh(new AdvancedJavaCloseable)
  //closeShh(new HipsterCloseable)

  // using structural types as standalone types
  def altClose(closeable: { def close(): Unit}): Unit = closeable.close()

  // type-checking => duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
    def sit(): Unit = println("Ok")
  }

  class Car {
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  //static duck typing

  // CAVEAT: based on reflection

  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZZ!!!"
  }

  def f[T](somethingWithAHead: { def head: T}): Unit = println(somethingWithAHead.head)

  case object CBNil extends CBL[Nothing] {
    override def head: Nothing = new Nothing
    override def tail: CBL[Nothing] = this
  }

  case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

  f(CBCons(2, CBNil))
  f(new Human) // T = ?  => T = Brain

  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  val brainsList = CBCons(new Brain, CBNil)
  HeadEqualizer.===(brainsList, new Human)
  HeadEqualizer === (new Human, brainsList)

  val stringsList = CBCons("Brains", CBNil)
  HeadEqualizer.===(new Human, stringsList) // not type safe
}
