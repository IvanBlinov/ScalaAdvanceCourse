package lectures.part3concurrency

object Intro extends App {

  //JVM threads
  /*val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start()
  //create a JVM thread
  aThread.join() //blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))
  threadHello.start()
  threadGoodbye.start()

  //executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("Almost done")
    Thread.sleep(1000)
    println("Done after 2 second")
  })

  pool.shutdown()
  //pool.execute(() => println("Should not appear")) // throws an exception

  //pool.shutdownNow()
  println(pool.isShutdown)

  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  for (_ <- 1 to 100) runInParallel*/
  //race condition

  /*class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    println("I've bought " + thing)
    println("My account is now " + account)
  }

  for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    println()
  }

  // option #1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized({
      // no two threads can evaluate this at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("My account is now " + account)
    })
  }*/

  // option #2: use @volatile
  class BankAccountVolatile(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  /**
    * 1) Construct 50 "inception" threads
    *     Thread1 -> Thread2 -> Thread3
    *     println("Hello form thread #3")
    *     in REVERSE ORDER
    *
    *
    */
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })

  inceptionThreads(50).start()

  var x = 0
  val thread = (1 to 100).map(_ => new Thread(() => x += 1))
  thread.foreach(_.start())

  //sleep fallacy
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  awesomeThread.join()
  println(message)

}
