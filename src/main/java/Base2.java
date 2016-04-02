
/**
 * Class description goes here.
 * 
 * @author aopfilter@163.com
 * @since Nov 6, 2015
 * @version 1.0
 * @see http://blog.sina.com.cn/s/blog_4cc16fc50100bjjp.html
 */
public class Base2 {
  private String baseName = "base";

  public Base2() {
    System.out.println("Base2.Base()" + baseName);
    callName();
  }

  public void callName() {
    System.out.println("Base2.callName()" + baseName);
  }

  static class Sub extends Base2 {
    private String baseName = "sub";

    public Sub() {
      System.out.println("Base2.Sub.Sub()" + baseName);
    }

    @Override
    public void callName() {
      System.out.println("Base2.Sub.callName()" + baseName);
    }
  }

  public static void main(String[] args) {
    Base2 b = new Sub();
    System.out.println("Base2.main()" + b);
  }
}
