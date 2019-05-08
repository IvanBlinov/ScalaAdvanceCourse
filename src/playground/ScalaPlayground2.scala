package playground

object ScalaPlayground2 {

  def main(args: Array[String]): Unit = {
    def isSorted[A](array: Array[A], ordered: (A, A) => Boolean): Boolean = {
      def sortedHelper(array: Array[A], index: Int, ordered: (A, A) => Boolean): Boolean = {
        if (index >= array.length - 1) true
        else if (!ordered(array(index), array(index + 1))) {
          false
        } else sortedHelper(array, index + 1, ordered)
      }

      sortedHelper(array, 0, ordered)
    }

    println(isSorted(Array(1,2,3), (x: Int, y: Int) => x > y))
    println(isSorted(Array(1,2,3), (x: Int, y: Int) => x < y))
  }

  def partial1[A,B,C](a: A, f: (A, B) => C): B => C = {
    f(a, _)
  }

  val i = 1
  val f = (a: Int, b: String) => b.charAt(a)
  val func1 = partial1(i, f)
  println(func1("Hello, Scala"))

  def curry[A,B,C](f: (A, B) => C): A => B => C = {
    a => b => f(a, b)
  }

  val curriedf = curry((a: String, b: Int) => a.charAt(b))
  val curriedStr = curriedf("Hello")
  println(curriedStr(3))
  println(curriedf("String")(3))

  def uncarry[A,B,C](f: A => B => C): (A,B) => C = {
    (a, b) => f(a)(b)
  }

  def compose[A,B,C](f: B => C, g: A => B): A => C = {
    a => f(g(a))
  }

  trait MyList[+A]
  case object MyEmptyList extends MyList[Nothing]
  case class MyCons[+A](h: A, t: MyList[A]) extends MyList[A]

  object MyList {

    def tail[A](l: MyList[A]): MyList[A] =
      l match {
        case MyEmptyList => MyEmptyList
        case MyCons(_, tail) => tail
      }

    def setHead[A](list: MyList[A], newHead: A): MyList[A] = MyCons(newHead, list)

    def drop[A](list: MyList[A], n: Int): MyList[A] =
      if (n <= 0) list
      else
      list match {
        case MyEmptyList => MyEmptyList
        case MyCons(_, tail) => drop(tail, n - 1)
      }

    def dropWhile[A](list: MyList[A], predicate: A => Boolean): MyList[A] =
      list match {
        case MyCons(head, tail) if predicate(head) => dropWhile(tail, predicate)
        case _ => list
      }
  }

  val myList = MyCons(3, MyCons(4, MyEmptyList))
}
