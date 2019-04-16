package lectures.part4implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MagnetPattern extends App {

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    //def receive(future: Future[P2PResponse]): Int NOT POSSIBLE
    //lots of overloads
  }

  /*
  1 - type arasure
  2 - lifting doesn't work
    val receive = receive _
  3 - code duplication
  4 - type inference and default args
    actor.receive(?!)
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()
  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PRequest
      println("Handling P2P Request")
      42
    }
  }

  implicit class FromP2PResponse(request: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PResponse
      println("Handling P2P Response")
      24
    }
  }
  receive(new P2PRequest)
  receive(new P2PResponse)

  //1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }
  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }
  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  //2 - lifting
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add(s: String): Int = s.toInt + 1
    //add1 overloads
  }

  //"magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }
  implicit class AddString(x: String) extends AddMagnet {
    override def apply(): Int = x.toInt + 1
  }
  val add = add1 _
  println(add(1))
  println(add("3"))

  //val receiveFV = receive _
  //receiveFV(new P2PResponse)

  /*
    Drawbacks
    1 - verbose
    2 - hard to read
    3 - you can't name or place default arguments
    4 - call by name doesn't work correctly
   */

  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
    //other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Scala")
    "hahaha"
  }

  handle(sideEffectMethod())
  handle {
    println("Scala")
    "hahaha"
  }
  //Equivalent
  handle {
    println("Scala")
    new StringHandle("hahaha")
  }
  //careful
}
