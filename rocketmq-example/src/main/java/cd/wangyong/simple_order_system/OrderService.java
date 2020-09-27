package cd.wangyong.simple_order_system;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cd.wangyong.simple_order_system.entity.OrderEntity;
import cd.wangyong.simple_order_system.repo.OrderRepository;
import cd.wangyong.simple_order_system.valobj.OrderOperation;

@Service
public class OrderService implements InitializingBean, DisposableBean, TransactionListener {
    private static final String TOPIC = "Order";
    private static final String TAG = "SubmitOrder";
    private static final String KEY_PREFIX = "SubmitOrder-";

    private final TransactionMQProducer producer = new TransactionMQProducer("transaction-submit-order");
    private final ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("submit-order-transaction-msg-check-thread");
                    return thread;
                }
            });

    @Autowired
    private OrderRepository orderRepository;

    public void submitOrderInTransaction(OrderEntity orderEntity) throws MQClientException, UnsupportedEncodingException {
        Message msg =
                new Message(TOPIC, TAG, KEY_PREFIX + orderEntity.getOrderId(), JSON.toJSONString(orderEntity).getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.sendMessageInTransaction(msg, new OrderOperation(orderEntity, OrderOperation.SUBMIT));
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            if (arg instanceof OrderOperation) {
                OrderOperation orderOperation = (OrderOperation) arg;
                if (Objects.equals(orderOperation.getOperation(), OrderOperation.SUBMIT)) {
                    submitOrderLocally(orderOperation.getOrderEntity(), msg.getTransactionId());
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                else if (Objects.equals(orderOperation.getOperation(), OrderOperation.CONFIRM)) {
                    confirmOrderLocally(orderOperation.getOrderEntity(), msg.getTransactionId());
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                else if (Objects.equals(orderOperation.getOperation(), OrderOperation.CANCEL)) {
                    cancelOrderLocally(orderOperation.getOrderEntity(), msg.getTransactionId());
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
            }
        } finally {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    private void submitOrderLocally(OrderEntity orderEntity, String transactionId) {
        orderEntity.setTransactionId(transactionId);
        orderRepository.insertOrder(orderEntity);
    }

    private void confirmOrderLocally(OrderEntity orderEntity, String transactionId) {
        // TODO: 2020/9/25
    }

    private void cancelOrderLocally(OrderEntity orderEntity, String transactionId) {
        // TODO: 2020/9/25
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        // TODO: 2020/9/25 queryOrderLocally
        OrderEntity orderEntity = orderRepository.queryByTransactionId(msg.getMsgId());
        if (orderEntity == null) return LocalTransactionState.COMMIT_MESSAGE;
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        producer.setTransactionListener(this);
        producer.setExecutorService(executorService);
        producer.start();
    }

    @Override
    public void destroy() throws Exception {
        producer.shutdown();
    }
}
