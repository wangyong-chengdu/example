package cd.wangyong.concurrent_example.互斥.预防死锁;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andy
 * @since 2020/12/19
 */
public class 破坏占用且等待 {
    // 资源分配器1
    static class Allocator {
        private List<Object> locks = new ArrayList<>();

        public synchronized boolean apply(Object from, Object to) {
            if (locks.contains(from) || locks.contains(to)) {
                return false;
            }
            else {
                locks.add(from);
                locks.add(to);
            }
            return true;
        }

        public synchronized void free(Object from, Object to) {
            locks.remove(from);
            locks.remove(to);
        }
    }

    // 资源分配器2：使用等待通知机制优化
    static class Allocator2 {
        private List<Object> locks = new ArrayList<>();

        public synchronized void apply(Object from, Object to) {
            if (locks.contains(from) || locks.contains(to)) {
                try {
                    wait();
                } catch (Exception e) {

                }
            }
            else {
                locks.add(from);
                locks.add(to);
            }
        }

        public synchronized void free(Object from, Object to) {
            locks.remove(from);
            locks.remove(to);
            notifyAll();
        }
    }


    static class Account {
        // 保证分配者单例
        private Allocator allocator;
        private int balance;

        /**
         * 转账：策略：一次性申请锁，避免死锁发生
         * @param target
         * @param amount
         */
        public void transfer(Account target, int amount) {
            while(!allocator.apply(this, target));

            try {
                synchronized (this) {
                    synchronized (target) {
                        if (this.balance > amount) {
                            this.balance -= amount;
                            target.balance += amount;
                        }
                    }
                }
            }
            finally {
                allocator.free(this, target);
            }

        }
    }
}
