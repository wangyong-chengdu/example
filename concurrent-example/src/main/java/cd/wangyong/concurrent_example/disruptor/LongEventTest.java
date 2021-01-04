package cd.wangyong.concurrent_example.disruptor;


import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class LongEventTest {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 注册消费者
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        // 注册生产者
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            Thread.sleep(1000);
        }
    }
}
