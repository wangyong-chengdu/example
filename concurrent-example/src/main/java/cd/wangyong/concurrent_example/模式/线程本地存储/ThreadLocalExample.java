package cd.wangyong.concurrent_example.模式.线程本地存储;

import java.lang.ref.WeakReference;

/**
 * @author andy
 * @since 2020/12/27
 */
public class ThreadLocalExample {
    class MyThread extends Thread {
        MyThreadLocal.MyThreadLocalMap threadLocals;
    }

    static class MyThreadLocal<T> {
        static class MyThreadLocalMap {
            MyEntry[]  table;
            MyEntry getEntry(MyThreadLocal key) {
                // TODO: 2020/12/27
                return null;
            }
        }

        static class MyEntry extends WeakReference<MyThreadLocal> {
            Object value;
            public MyEntry(MyThreadLocal referent) {
                super(referent);
            }
        }

        public T get() {
            MyThread myThread = (MyThread) Thread.currentThread();
            MyThreadLocalMap map = myThread.threadLocals;
            MyEntry entry = map.getEntry(this);
            return (T) entry.value;
        }
    }
}
