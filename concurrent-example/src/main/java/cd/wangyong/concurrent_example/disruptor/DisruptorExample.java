package cd.wangyong.concurrent_example.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * @author andy
 * @since 2020/12/31
 */
public class DisruptorExample {

    /**
     * 自定义Event
     */
    static class LongEvent {
        private long value;
        public void set(long value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 指定RingBuffer大小，必须是2的N次方
        int bufferSize = 1024;
        // 构造disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
        // 注册事件处理器
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("E: " + event));

        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 生产Event
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long l = 0L; true; l++) {
            byteBuffer.putLong(0, l);
//            ringBuffer.publishEvent((event, sequence, buffer) -> event.set((buffer.getLong(0)), byteBuffer);
            Thread.sleep(1000);
        }
    }
}
