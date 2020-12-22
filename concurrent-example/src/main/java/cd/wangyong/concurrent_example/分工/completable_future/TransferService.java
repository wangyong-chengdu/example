package cd.wangyong.concurrent_example.分工.completable_future;

import java.util.concurrent.CompletableFuture;

/**
 * 转账服务
 * @author andy
 * @since 2020/10/15
 */
public interface TransferService {
    /**
     * 转账
     * @param fromAccount 转出账户
     * @param toAccount 转入账户
     * @param amount 金额，单位分
     */
    CompletableFuture<Void> transfer(int fromAccount, int toAccount, int amount);

}
