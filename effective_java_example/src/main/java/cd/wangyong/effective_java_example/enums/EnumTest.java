package cd.wangyong.effective_java_example.enums;

/**
 * @author andy
 * @since 2020/10/9
 */
public class EnumTest {

    public static enum CustomDigit {
        ZERO,ONE,TWO,THREE,FOUR;
    }

    public static void main(String[] args) {
        System.out.println(CustomDigit.values()[0]);
    }

}
