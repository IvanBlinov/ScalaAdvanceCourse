package lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com").toHtml
  /*
    1 - for the types WE write
    2 - ONE implementation out of quite a number
   */

  //option pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) =  value match {
      case User(name, age, email) =>
      case _ =>
    }
  }
  /*
    1 - lost type safety
    2 - modify code every time
    3 - still ONE implementation
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))

  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div><${date.toString}></div>"
  }

  //2 - multiple serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  //TYPE class
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

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

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = {
      serializer.serialize(value)
    }

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  //println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  //access to entire type class interface
  println(HTMLSerializer[User].serialize(john))

  println(Equal[User].equal(john, john))
  println(EqualAnother(john, john))
  //AD-HOC polymorphism
}
