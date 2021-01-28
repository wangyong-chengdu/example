package cd.wangyong.concurrent_example.Java线程;

import java.util.Arrays;

/**
 * 最小Java程序到底会创建几个线程：
 线程：Reference Handler
 线程：Finalizer
 线程：Signal Dispatcher
 线程：main
 线程：Monitor Ctrl-Break
 * @author andy
 * @since 2021/1/16
 */
public class ThreadExample {
    public static void main(String[] args) {
        System.out.println("hello, world.");

        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup p = group;
        while (group != null) {
            p = group;
            group = group.getParent();
        }

        assert p != null;
        int threadNum = p.activeCount();
        Thread[] threads = new Thread[threadNum];
        p.enumerate(threads);

        Arrays.stream(threads).forEach(thread -> System.out.println("线程：" + thread.getName()));
    }
}
