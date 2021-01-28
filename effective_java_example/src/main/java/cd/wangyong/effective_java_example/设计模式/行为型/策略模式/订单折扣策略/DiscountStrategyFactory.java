package cd.wangyong.effective_java_example.设计模式.行为型.策略模式.订单折扣策略;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andy
 * @since 2021/1/26
 */
public class DiscountStrategyFactory {
    private static final Map<OrderType, DiscountStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(OrderType.NORMAL, new NormalDiscountStrategy());
        strategyMap.put(OrderType.GROUPON, new GrouponDiscountStrategy());
        strategyMap.put(OrderType.PROMOTION, new PromotionDiscountStrategy());
    }

    public static DiscountStrategy getDiscountStrategy(OrderType orderType) {
        return strategyMap.get(orderType);
    }
}
