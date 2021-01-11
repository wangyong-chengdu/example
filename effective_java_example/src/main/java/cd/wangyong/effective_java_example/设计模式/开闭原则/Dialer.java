package cd.wangyong.effective_java_example.设计模式.开闭原则;

/**
 * 拨号器
 * @author andy
 * @since 2021/1/7
 */
public class Dialer {
    public void enterDigit(int digit) {
        System.out.println("enter digit: " + digit);
    }

    public void dial() {
        System.out.println("dialing...");
    }
}
