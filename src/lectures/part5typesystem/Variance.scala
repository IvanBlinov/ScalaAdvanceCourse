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

  /*
    Invariant, covariant, contravariant
      Parking[T](things List[T]) {
        park(vehicle: T)
        impound(vehicles: List[T])
        checkVehicles(conditions: String): List[T]
      }

      2. IList[T]
      3. Parking = monad!
        -flatMap
   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class CParking[+T](things: List[T]) {
    def park[B >: T](vehicle: B): CParking[B] = ???
    def impound[B >: T](vehicles: List[B]): CParking[B] = ???
    def checkVehicles(conditions: String): List[T] = List()

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  val list = List(new Bike, new Car)
  val cparking = new CParking[Bike](List(new Bike))
  cparking.park(new Bike)
  val carParking = cparking.park(new Car)
  val vehicleParking = cparking.park(new Vehicle)
  //val cparking = new CParking[Bike](list)

  class IParking[T](things: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = List()

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  val iparking = new IParking[Bike](List(new Bike))
  iparking.park(new Bike)
  //iparking.park(new Car)
  //iparking.park(new Vehicle)
  //val iparking = new IParking[Bike](List(new Bike, new Car))

  class XParking[-T](things: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[B <: T](conditions: String): List[B] = List()

    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
  }

  val xparking = new XParking[Vehicle](List(new Bike, new Car))
  xparking.park(new Vehicle)
  xparking.park(new Car)
  xparking.park(new Bike)

  /*
    Rule of thumb
    - use covariance = COLLECTION OF THINGS
    - user contravariance = GROUP OF ACTIONS
   */
  // ----------------
  class CParkingCustom[+T](things: IList[T]) {
    def park[B >: T](vehicle: B): CParkingCustom[B] = ???
    def impound[B >: T](vehicles: IList[B]): CParkingCustom[B] = ???
    def checkVehicles[B >: T](conditions: String): IList[B] = new IList
  }

  class IParkingCustom[T](things: IList[T]) {
    def park(vehicle: T): IParkingCustom[T] = ???
    def impound(vehicles: IList[T]): IParkingCustom[T] = ???
    def checkVehicles(conditions: String): IList[T] = new IList
  }

  class XParkingCustom[-T](things: IList[T]) {
    def park(vehicle: T): XParkingCustom[T] = ???
    def impound[B <: T](vehicles: IList[B]): XParkingCustom[B] = ???
    def checkVehicles[B <: T](conditions: String): IList[B] = new IList
  }

  class IList[I]
}
