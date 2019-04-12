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

  object UserSerializer extends HTMLSerializer[User] {
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

  /**
    * Equal type class
    */
  trait Equal[T] {
    def equal(obj1: T, obj2: T): Boolean
  }

  object LoggedUserEqual extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = {
      user1.email == user2.email &&
      user1.name == user2.email &&
      user1.age == user2.age
    }
  }

  object UnloggedUserEqual extends Equal[User] {
    override def equal(user1: User, user2: User): Boolean = user1.email.equals(user2.email)
  }
}
