package lectures.part5typesystem

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  //per-instance
  val outer = new Outer
  //val inner = new Inner
  //val inner = new Outer.Inner
  val inner = new outer.Inner

  val oo =new Outer
  //val otherInner: oo.Inner = new outer.Inner CAN'T

  outer.print(inner)
  //oo.print(inner)

  //path-dependent types

  // Outer#Inner
  outer.printGeneral(inner)
  oo.printGeneral(inner)

  /*
    Exercise
    DB keyed by Int or String, but maybe others
   */

  trait Item {
    type key
  }
  trait IntItem extends Item {
    override type key = Int
  }
  trait StringItem extends Item {
    override type key = String
  }

  trait Item2[K] extends Item {
    override type key = K
  }

  trait IntItem2 extends Item2[Int]
  trait StringItem2 extends Item2[String]

  def get[ItemType <: Item](key: ItemType#key): ItemType = ???

  get[IntItem](42)
  get[StringItem]("home")
  get[IntItem2](24)
  //get[IntItem2]("String")
  //get[IntItem]("Scala")
}
