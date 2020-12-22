package cd.wangyong.concurrent_example.分工.completable_future;

import java.util.concurrent.CompletableFuture;

/**
 * 账户服务
 * @author andy
 * @since 2020/10/15
 */
public interface AccountService {
    /**
     * 变更账户金额
     * @param account 账户ID
     * @param amount 金额，单位分
     * @return
     */
    CompletableFuture<Void> add(int account, int amount);
}
