package cd.wangyong.simple_order_system.valobj;


import cd.wangyong.simple_order_system.entity.OrderEntity;

public final class OrderOperation {
    public static final String SUBMIT = "submit";
    public static final String CONFIRM = "confirm";
    public static final String CANCEL = "cancel";

    private final OrderEntity orderEntity;
    private final String operation;

    public OrderOperation(OrderEntity orderEntity, String operation) {
        this.orderEntity = orderEntity;
        this.operation = operation;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public String getOperation() {
        return operation;
    }
}
