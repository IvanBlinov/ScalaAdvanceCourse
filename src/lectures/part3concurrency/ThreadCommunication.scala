package lectures.part3concurrency

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

  smartProdCons()
}
