package exercises

trait Lazy[+A] {
  def flatMap[B](f: A => Lazy[B]): Lazy[B]
}
object Lazy{
  def apply[A](a: => A): Lazy[A] = {

  }
}

class Monad[A] {
  def flatMap[B](f: A => Monad[B]): Monad[B] = ???

  def map[B](f: A => B): Monad[B] = ???
  def flatten(m: Monad[Monad[A]]): Monad[A] = m.map()
}
