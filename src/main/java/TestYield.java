
/**
 * @see http://www.importnew.com/14958.html
 * 
 */
public class TestYield {

  public static void main(String[] args) {
    Thread producer = new Producer2();
    Thread consumer = new Consumer2();

    // producer.setPriority(Thread.MIN_PRIORITY); // Min Priority
    // consumer.setPriority(Thread.MAX_PRIORITY); // Max Priority

    producer.start();
    consumer.start();
  }
}


class Producer2 extends Thread {
  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      System.out.println("I am Producer : Produced Item " + i);
      Thread.yield(); // 当前正在执行的线程把运行机会交给线程池中拥有相同优先级的线程
    }
  }
}


class Consumer2 extends Thread {
  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {
      System.out.println("I am Consumer : Consumed Item " + i);
      Thread.yield();
    }
  }
}
