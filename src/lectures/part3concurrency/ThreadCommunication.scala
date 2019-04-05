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

  prodConsLargeBuffer()
}
