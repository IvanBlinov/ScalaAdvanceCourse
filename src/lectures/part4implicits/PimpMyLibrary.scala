package lectures.part4implicits

object PimpMyLibrary extends App {

  implicit class RichInt(val value: Int) {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit) = {
      def timesAux(n: Int): Unit = {
        if (n <= 0) {
          ()
        } else {
          function()
          timesAux(n - 1)
        }
      }
      timesAux(value)
      /*1 to value {
        function
      }*/
    }

    def *[T](list: List[T]) = {
      def multipleHelper(n: Int, accumulator: List[T]): List[T] = {
        if (n <= 0) {
          accumulator
        } else {
          multipleHelper(n - 1, accumulator ++ list)
        }
      }
      multipleHelper(value, List())
    }
  }

  new RichInt(42).sqrt
  42.isEven // new RichInt(42).isEven
  //type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiler doesn't do multiple implicit searches
  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
  //42.isOdd

  implicit class MyString(s: String) {
    def asInt(): Int = Integer.valueOf(s)
    def encrypt(length: Int): String = s.map(c => (c + length).asInstanceOf[Char])
  }

  println("3".asInt() + 4)
  println("John".encrypt(2))

  3.times(() => println("Scala Rocks"))
  println(4 * List(1,2,3))

  // "3" / 4
  implicit def stringToInt(string: String): Int = {
    Integer.valueOf(string)
  }
  println("6" / 2) // stringToInt("6") / 2

  //equivalent implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int) {
    implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)
  }

  //danger zone
  implicit def intToBoolean(value: Int): Boolean = value == 1

  val aConditionValue = if (3) "OK" else "Something wrong"
  println(aConditionValue)

}
