import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/*
 * http://www.importnew.com/16453.html
 * 
 */
public class ProducerConsumer {

  public static void main(String args[]) {
    Queue<Integer> buffer = new LinkedList<Integer>();
    int maxSize = 10;

    Thread producer = new Producer(buffer, maxSize, "PRODUCER");
    Thread consumer = new Consumer(buffer, maxSize, "CONSUMER");
    producer.start();
    consumer.start();
  }
}


/**
 * * Producer Thread will keep producing values for Consumer * to consumer. It will use wait()
 * method when Queue is full * and use notify() method to send notification to Consumer * Thread. *
 * * @author WINDOWS 8 *
 */
class Producer extends Thread {

  private Queue<Integer> queue;
  private int maxSize;

  public Producer(Queue<Integer> queue, int maxSize, String name) {
    this.queue = queue;
    this.maxSize = maxSize;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (queue) {
        while (queue.size() == maxSize) {
          try {
            System.out.println("Queue is full, " + "Producer thread waiting for "
                + "consumer to take something from queue");

            queue.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        queue.add(new Random().nextInt());
        queue.notifyAll();
      }
    }
  }
}


/**
 * * Consumer Thread will consumer values form shared queue. * It will also use wait() method to
 * wait if queue is * empty. It will also use notify method to send * notification to producer
 * thread after consuming values * from queue. * * @author WINDOWS 8 *
 */
class Consumer extends Thread {

  private Queue<Integer> queue;
  private int maxSize;

  public Consumer(Queue<Integer> queue, int maxSize, String name) {
    this.queue = queue;
    this.maxSize = maxSize;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (queue) {
        while (queue.isEmpty()) {
          System.out.println("Queue is empty," + "Consumer thread is waiting"
              + " for producer thread to put something in queue");
          try {
            queue.wait();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        System.out.println("Consuming value : " + queue.remove());
        queue.notifyAll();
      }
    }
  }
}

