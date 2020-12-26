package cd.wangyong.concurrent_example.分工;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 统计单词数量
 * @author andy
 * @since 2020/12/26
 */
public class WordsComputeWithForkJoinPool {

    /**
     * MapReduce模拟
     */
    static class MR extends RecursiveTask<Map<String, Long>> {
        private final String[] contents;
        private final int start;
        private final int end;

        public MR(String[] contents, int start, int end) {
            this.contents = contents;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Long> compute() {
            if (end <= start) {
                return Collections.emptyMap();
            }
            else if (end - start == 1) {
                return computeByLine(contents[start]);
            }

            int mid = start + (end - start) / 2;
            MR mr1 = new MR(contents, start, mid);
            mr1.fork();

            MR mr2 = new MR(contents, mid, end);
            return merge(mr2.compute(), mr1.join());
        }

        /**
         * 按行统计
         */
        private Map<String, Long> computeByLine(String line) {
            Map<String, Long> map = new HashMap<>();
            String [] words = line.split("\\s+");
            Arrays.stream(words).forEach(word -> map.put(word, map.getOrDefault(word, 0L) + 1));
            return map;
        }

        /**
         * 合并2个结果集
         */
        private Map<String, Long> merge(Map<String, Long> m1, Map<String, Long> m2) {
            Map<String, Long> map = new HashMap<>(m1);
            m2.forEach((k, v) -> map.put(k, map.getOrDefault(k, 0L) + v));
            return map;
        }
    }

    public static void main(String[] args) {
        String[] contents = {"hello world", "hello me", "hello fork", "hello join", "for join in world"};

        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        MR mr = new MR(contents, 0, contents.length);
        Map<String, Long> map = forkJoinPool.invoke(mr);
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }

}
