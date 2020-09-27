package cd.wangyong.simple_order_system.repo;

import org.springframework.stereotype.Component;

import cd.wangyong.simple_order_system.entity.OrderEntity;


@Component
public interface OrderRepository {

    boolean insertOrder(OrderEntity orderEntity);
    OrderEntity queryByTransactionId(String transactionId);
}
