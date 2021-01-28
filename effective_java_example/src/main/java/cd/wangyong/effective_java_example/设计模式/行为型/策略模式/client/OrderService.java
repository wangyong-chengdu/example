package cd.wangyong.effective_java_example.设计模式.行为型.策略模式.client;

import cd.wangyong.effective_java_example.设计模式.行为型.策略模式.订单折扣策略.DiscountStrategy;
import cd.wangyong.effective_java_example.设计模式.行为型.策略模式.订单折扣策略.DiscountStrategyFactory;
import cd.wangyong.effective_java_example.设计模式.行为型.策略模式.订单折扣策略.Order;

/**
 * @author andy
 * @since 2021/1/26
 */
public class OrderService {

    public double discount(Order order) {
        DiscountStrategy discountStrategy = DiscountStrategyFactory.getDiscountStrategy(order.getType());
        return discountStrategy.calDiscount(order);
    }

}
