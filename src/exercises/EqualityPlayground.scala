package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

  /**
    * Equal type class
    */
  trait Equal[T] {
    def equal(obj1: T, obj2: T): Boolean
  }

  object Equal {
    def apply[T] (implicit instance: Equal[T]) = instance
  }

  object EqualAnother {
    def apply[T] (a: T, b: T)(implicit instance: Equal[T]) = instance.equal(a,b)
  }

  implicit object LoggedUserEqual extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = {
      user1.email == user2.email &&
        user1.name == user2.name &&
        user1.age == user2.age
    }
  }

  object UnloggedUserEqual extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = user1.email.equals(user2.email)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(Equal[User].equal(john, john))
  println(EqualAnother(john, john))
  //AD-HOC polymorphism

  implicit class SuperEquality[T](value: T) {
    def ===(anotherValue: T)(implicit equal : Equal[T]): Boolean = equal.equal(value, anotherValue)
    def !==(anotherValue: T)(implicit equal : Equal[T]): Boolean = !equal.equal(value, anotherValue)
  }

  println(john === john) //new SuperEquality[User](john).===(john)(LoggedUserEqual)
  println(john !== john) //new SuperEquality[User](john).!==(john)(LoggedUserEqual)
  // TYPE SAFE
  //john == 42 OK
  //john === 42 WON'T COMPILE!
}
