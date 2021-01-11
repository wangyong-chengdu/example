package cd.wangyong.effective_java_example.设计模式.开闭原则;

/**
 * 手机
 * @author andy
 * @since 2021/1/7
 */
public class Phone {
    /**
     * 拨号器
     */
    private Dialer dialer;
    /**
     * 数字按键
     */
    private Button[] digitButtons;

    /**
     * 拨号按键
     */
    private Button sendButton;

    public Phone() {
        dialer = new Dialer();
        digitButtons = new Button[10];

        for (int i = 0; i < digitButtons.length; i++) {
            digitButtons[i] = new Button();
            final int digit = i;
            digitButtons[i].addListener(() -> dialer.enterDigit(digit));
        }

        sendButton = new Button();
        sendButton.addListener(() -> dialer.dial());
    }

    public static void main(String[] args) {
        Phone phone = new Phone();
        phone.digitButtons[9].press();
        phone.digitButtons[1].press();
        phone.digitButtons[1].press();

        phone.sendButton.press();
    }
}
