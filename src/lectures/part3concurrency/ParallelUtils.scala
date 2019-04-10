package lectures.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.immutable.ParVector
import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}

object ParallelUtils extends App {

  //1 - Parallel collections
  val parList = List(1,2,3).par
  val parVector = ParVector[Int](1,2,3)

  /*
    Seq
    Vector
    Array
    Map - HashMap, Trie
    Set - Hash, Trie
   */
  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 1000000).toList
  val serialTime = measure {
    list.map(_ + 1)
  }
  println("Serial time: " + serialTime)
  val parallelTime = measure {
    list.par.map(_ + 1)
  }
  println("Parallel time: " + parallelTime)

  //be careful with fold and reduce in parallel
  println(List(1,2,3).reduce(_ - _))
  println(List(1,2,3).par.reduce(_ - _))

  //synchronization
  var sum = 0
  List(1,2,3).par.foreach(sum += _)
  println(sum)

  //configuring
  parVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
    ThreadPoolTaskSupport - deprecated
    ExecutionContextTaskSupport(EC)
   */

  parVector.tasksupport = new TaskSupport {
    override val environment: AnyRef = _
    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
    override def parallelismLevel: Int = ???
  }

  // 2 - atomic ops and references
  val atomic = new AtomicReference[Int](2)
  val currentValue = atomic.get() // thread safe
  atomic.set(4)
  atomic.getAndSet(5) // thread safe combo
  atomic.compareAndSet(38, 56)
  // if the value if 38 set 56 or do nothing
  //reference equality

  atomic.updateAndGet(_ + 1) //thread safe function
  atomic.getAndUpdate(_ + 1)

  atomic.accumulateAndGet(12, _ + _)
  atomic.getAndAccumulate(12, _ + _)
}
