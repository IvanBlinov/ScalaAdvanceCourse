package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 //Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  }
  // {1,2,5] => Int
  // partial function

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  } //partial function value
  println(aPartialFunction(2))
  //println(aPartialFunction(4))

  println(aPartialFunction.isDefinedAt(57))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal function

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000 // if comment then will crash
    case 5 => 1000
  }

  println(aMappedList)

  val chatBot: PartialFunction[String, String] = {
    case "Hello" => "Hello, how are you?"
    case "Fine" | "I'm ok" => "Nice to hear it"
    case "Bad" => "What happened?"
    case "Feel bad" => "Oh, I'm so sorry"
    case _ => "Sorry, I don't understand what are you saying"
  }

  val chatBot2 = new PartialFunction[String, String] {

    override def isDefinedAt(x: String): Boolean = true

    override def apply(v1: String): String = v1 match {
      case "Hello" => "Hello, how are you?"
      case "Fine" | "I'm ok" => "Nice to hear it"
      case "Bad" => "What happened?"
      case "Feel bad" => "Oh, I'm so sorry"
      case _ => "Sorry, I don't understand what are you saying"
    }
  }

  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
}
