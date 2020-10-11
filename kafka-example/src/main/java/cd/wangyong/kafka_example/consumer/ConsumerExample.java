package cd.wangyong.kafka_example.consumer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author andy
 * @since 2020/10/7
 */
public class ConsumerExample {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final ExecutorService executorService = new ThreadPoolExecutor(4, 8,
            100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Kafka-Consumer-Thread-" + counter.getAndIncrement());
            return thread;
        }},
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> consume());
        }

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
    }

    public static void consume() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("quickstart-events"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            records.forEach(record -> System.out.printf(Thread.currentThread().getName() + " offset = %d, key = %s, value = %s， timestamp = %d%n", record.offset(), record.key(), record.value(), record.timestamp()));
        }
    }
}
