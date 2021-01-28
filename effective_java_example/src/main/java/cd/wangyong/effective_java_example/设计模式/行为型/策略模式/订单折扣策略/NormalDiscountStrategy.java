package cd.wangyong.effective_java_example.设计模式.行为型.策略模式.订单折扣策略;

/**
 * @author andy
 * @since 2021/1/26
 */
public class NormalDiscountStrategy implements DiscountStrategy {
    @Override
    public double calDiscount(Order order) {
        return 0;
    }
}
