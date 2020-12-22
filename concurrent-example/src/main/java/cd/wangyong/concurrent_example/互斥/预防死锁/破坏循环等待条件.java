package cd.wangyong.concurrent_example.互斥.预防死锁;

/**
 * @author andy
 * @since 2020/12/19
 */
public class 破坏循环等待条件 {

    static class Account {
        private int id;
        private int balance;

        public void transfer(Account target, int amount) {
            Account left = this;
            Account right = target;

            if (this.id > target.id) {
                left = target;
                right = this;
            }

            synchronized (left) {
                synchronized (right) {
                    if (this.balance > amount) {
                        this.balance -= amount;
                        target.balance += amount;
                    }
                }
            }
        }
    }


}
