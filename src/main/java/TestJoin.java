
/**
 * @see http://www.importnew.com/16453.html
 * 
 */
public class TestJoin implements Runnable {

  public static int a = 0;

  @Override
  public void run() {
    for (int k = 0; k < 5; k++) {
      a = a + 1;
    }
  }

  public static void main(String[] args) throws Exception {
    Runnable r = new TestJoin();
    Thread t = new Thread(r);
    t.start();
    t.join(); // 当前线程（主线程）会等待此线程执行完后继续

    System.out.println(a);
  }
}


