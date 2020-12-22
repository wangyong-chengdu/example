package cd.wangyong.concurrent_example.互斥.lock;

/**
 * @author andy
 * @since 2020/12/19
 */
public class Account {
    private final Object balanceLock = new Object();
    private final Object passwordLock = new Object();

    private int balance;
    private String password;

    public void withdraw(int amount) {
        synchronized (balanceLock) {
            if (this.balance > amount) {
                this.balance -= amount;
            }
        }
    }

    public int getBalance() {
        synchronized (balanceLock) {
            return balance;
        }
    }

    public void updatePassword(String password) {
        synchronized (passwordLock) {
            this.password = password;
        }
    }

    public String getPassword() {
        synchronized (passwordLock) {
            return password;
        }
    }
}
