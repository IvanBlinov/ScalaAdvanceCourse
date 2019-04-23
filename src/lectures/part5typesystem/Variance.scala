package lectures.part5typesystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  //what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  //val cage: Cage[Animal] = new Cage[Cat] CAN'T

  // hell no - contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvarianceCage[T](val animal: T) // invariant

  //covariant position
  class CovariantCage[+T](val animal: T) // covariant position

  //class ContravariantCage[-T](val animal: T)
  // val catCage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Crocodile)

  //class CovariantVariable[+T](var animal: T) // types of vars in CONTRAVARIANT POSITION
  //val ccage: CCage[Animal] = new CCage[Cat](new Cat)
  //ccage.animal = new Crocodile

  //class ContravariantVariableCage[-T](var animal: T)
  // val catCage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Crocodile)

  class InvariantVariableCage[T](var animal: T) // ok

  trait AnotherCovariantCage[+T] {
    //def addAnimal(animal: T) // CONTRAVARIANT POSITION
  }
  // val ccage: CCage[Animal] = new CCage[Dog]
  // ccage.add(new Cat)

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  //acc.addAnimal(new Dog)
  acc.addAnimal(new Cat)
  //acc.addAnimal(new Animal)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] //widening the type
  }

  val emptyList = new MyList[Kitty]
  val animal = emptyList.add(new Kitty)
  val moreAnimals = animal.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  //METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
    //def get(isItaPuppy: Boolean): T //METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean): Animal = new Cat
      }

      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // EVIL CAT
    */
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  //val evilCat = shop.get(true, new Cat) COULDN'T DO THIS
  class TeraNova extends Dog
  val teraNova = shop.get(true, new TeraNova)
  /*
    BIG RULE
    1 - METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION
    2 - return types are in COVARIANT POSITION
   */
}
