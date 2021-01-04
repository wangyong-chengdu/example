package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * @author andy
 * @since 2021/1/4
 */
public class AccountExample {
    /**
     * 余额
     */
    private TxnRef<Integer> balance;

    public AccountExample(int balance) {
        this.balance = new TxnRef<>(balance);
    }

    public void transfer(AccountExample target, int amount) {
        STM.atomic(txn -> {
            Integer from = balance.getValue(txn);
            balance.setValue(from - amount, txn);
            Integer to = target.balance.getValue(txn);
            target.balance.setValue(to + amount, txn);
        });
    }

    public static void main(String[] args) {
        AccountExample a = new AccountExample(10);
        AccountExample b = new AccountExample(20);

        a.transfer(b, 5);

        System.out.println(a.balance.curRef.getValue());
        System.out.println(b.balance.curRef.getValue());
    }
}
