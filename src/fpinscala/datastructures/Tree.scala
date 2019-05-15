package fpinscala.datastructures

class Tree {

  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  def size[A](root: Tree[A]): Int = root match {
    case Leaf(_) => 1
    case Branch(l, r) => size(l) + size(r) + 1
  }

  def maximum(root: Tree[Int]): Int = root match {
    case Branch(l, r) => maximum(l).max(maximum(r))
    case Leaf(v) => v
  }

  def depth[A](root: Tree[A]): Int = root match {
    case Leaf(_) => 1
    case Branch(l, r) => (depth(l) max depth(r)) + 1
  }

  def map[A,B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(a) => Leaf(f(a))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  def fold[A,B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = t match {
    case Leaf(a) => f(a)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

  def foldSize[A](root: Tree[A]): Int = fold(root)(_ => 1)(1 + _ + _)

  def foldMaximum(root: Tree[Int]): Int = fold(root)(a => a)(_ max _)

  def foldDepth[A](root: Tree[A]): Int = fold(root)(_ => 1)((a1, a2) => (a1 max a2) + 1)

}
