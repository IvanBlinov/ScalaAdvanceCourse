package lectures.part2afp

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] = {
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
    }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] = {
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
    }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
    1. left-identity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x) // Make sense only for Success case
    Success(x).flatMap(f) = f(x) // proved.

    2. right-identity

    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    Fail(e).flatMap(...) = Fail(e)

    3. associativity

    attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

    Success(x).flatMap(f).flatMap(g) = f(x).flatMap(g) || Fail(e)
    Success(v).flatMap(v => f(v).flatMap(g)) = f(v).flatMap(g) || Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)


}
