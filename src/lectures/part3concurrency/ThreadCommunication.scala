package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
    producer - consumer problem
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmtpy: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmtpy) {
        println("[consumer] actively waiting...")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I've produced " + value)
      container.set(42)
    })

    consumer.start()
    producer.start()
  }

  //naiveProducerConsumer()

  def smartProdCons() = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized({
        container.wait()
      })
      println("[consumer] consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42

      container.synchronized({
        println("[producer] I've produced " + value)
        container.set(42)
        container.notify()
      })
    })

    consumer.start()
    producer.start()
  }

  //smartProdCons()

  /*
     producer -> [ ? ? ? ] -> consumer

   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumerThread = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer is empty, waiting...")
            buffer.wait()
          }

          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producerThread = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized({
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          println("[producer] producing " + i)
          buffer.enqueue(i)

          buffer.notify()
          i += 1
        })

        Thread.sleep(random.nextInt(500))
      }
    })

    consumerThread.start()
    producerThread.start()
  }

  //prodConsLargeBuffer()

  class ConsumerThread(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    val random = new Random()

    override def run(): Unit = {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer is empty, waiting...")
            buffer.wait()
          }

          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class ProducerThread(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    val random = new Random()

    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized({
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          buffer.notify()
          i += 1
        })

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def prodConsMultipleBuffer(nCt: Int, nPt: Int): Unit = {

    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 10
    (1 to nCt).foreach(i => new ConsumerThread(i, buffer).start())
    (1 to nPt).foreach(i => new ProducerThread(i, buffer, capacity).start())
  }

  //prodConsMultipleBuffer(4,4)

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized({
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!!!")
      })
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll")
      bell.synchronized({
        bell.notifyAll()
      })
    }).start()
  }

  //testNotifyAll()

  val a1, a2: Object = new Object
  val t1 = new Thread(() => {
    a1.synchronized{
      println("Synchronized by a1")
      Thread.sleep(200)
      a2.synchronized{
        Thread.sleep(200)
        println("Synchronized by a2")
      }
    }
  })

  val t2 = new Thread(() => {
    a2.synchronized{
      println("Synchronized by a2")
      Thread.sleep(200)
      a1.synchronized{
        Thread.sleep(200)
        println("Synchronized by a1")
      }
    }
  })

  //t1.start()
  //t2.start()

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized{
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized{
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"

    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while(this.side == other.side) {
        println(s"$this: Oh, but please $other, feel free to pass... ")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = new Friend("Sam")
  val pierre = new Friend("Pierre")

  //new Thread(() => sam.bow(pierre)).start()
  //new Thread(() => pierre.bow(sam)).start()

  //3 - livelock
  new Thread(() => sam.pass(pierre)).start()
  new Thread(() => pierre.pass(sam)).start()
}
