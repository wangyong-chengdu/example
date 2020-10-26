package cd.wangyong.canal2redis;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import redis.clients.jedis.Jedis;

/**
 * @author andy
 * @since 2020/10/26
 */
public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);
    private static final int BATCH_SIZE = 1000;

    public static void main(String[] args) {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(), 11111), "example", "", "");// example是Canal一个消息队列主题
        try(Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            connector.connect();
            logger.info("Connect 127.0.0.1:11111 success.");
            connector.subscribe(".*\\..*");
            logger.info("Subscribe example subject success.");
            connector.rollback();


            while (true) {
                Message message = connector.getWithoutAck(BATCH_SIZE);
                long batchId = message.getId();

                try {
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    else {
                        processEntries(message.getEntries(), jedis);
                    }
                    connector.ack(batchId); // 确认ack
                } catch (Throwable t) {
                    logger.error("Process canal message fail. batchId:{}", batchId);
                    connector.rollback(batchId); // 回滚
                }
            }
        }
        finally {
            connector.disconnect();
        }
    }

    private static void processEntries(List<CanalEntry.Entry> entries, Jedis jedis) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR, parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalEntry.EventType eventType = rowChange.getEventType();
            CanalEntry.Header header = entry.getHeader();
            logger.info("Binlog[{}:{}], name[{},{}], eventType:{}.", header.getLogfileName(), header.getLogfileOffset(),
                    header.getSchemaName(), header.getTableName(), eventType);

            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                // 删除
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    jedis.del(row2Key("user_id", rowData.getBeforeColumnsList()));
                }
                // 插入
                else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                    jedis.set(row2Key("user_id", rowData.getAfterColumnsList()), row2Value(rowData.getAfterColumnsList()));
                }
                // 更新
                else {
                    logger.info("Before");
                    printColumn(rowData.getBeforeColumnsList());

                    logger.info("After");
                    printColumn(rowData.getAfterColumnsList());

                    jedis.set(row2Key("user_id", rowData.getAfterColumnsList()), row2Value(rowData.getAfterColumnsList()));
                }
            }
        }
    }

    private static byte[] row2Value(List<CanalEntry.Column> columns) {
        Map<String, Object> map = columns.stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
        return new JSONObject(map).toString().getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] row2Key(String keyColumn, List<CanalEntry.Column> columns) {
        return columns.stream()
                .filter(column -> keyColumn.equals(column.getName()))
                .map(column -> column.getValue().getBytes(StandardCharsets.UTF_8))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Key column not found in the row!"));
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        columns.forEach(column -> logger.info("{} : {}   update={}", column.getName(), column.getValue(), column.getUpdated()));
    }

}
