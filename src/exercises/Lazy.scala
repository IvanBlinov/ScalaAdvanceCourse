package exercises

class Lazy[+A](value: => A) {
  private lazy val internalValue = value
  def use: A = internalValue
  def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = {
    f(internalValue)
  }
}
object Lazy{
  def apply[A](value: => A): Lazy[A] = {
    new Lazy[A](value)
  }
}

object LazyApp extends App {
  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  //println(lazyInstance.use)
  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use

  /*
    1. left-identity
    unit.flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)

    2. right-identity
    l.flatMap(unit) = l
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

    3. associativity
    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */
}

class Monad[A] {
  def flatMap[B](f: A => Monad[B]): Monad[B] = ???

  def map[B](f: A => B): Monad[B] = ??? //flatMap(x => new Monad[B](f(x)))
  def flatten(m: Monad[Monad[A]]): Monad[A] = m.flatMap((x: Monad[A]) => x)

  /*
    List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x* 2))
    List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap((x: List[Int]) => x) = List(1,2,3,4)
   */
}
