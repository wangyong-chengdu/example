package cd.wangyong.simple_transfer;

import java.util.concurrent.CompletableFuture;

public interface AccountService {
    /**
     * 变更账户金额
     * @param account 账户ID
     * @param amount 金额
     * @return
     */
    CompletableFuture<Void> add(int account, int amount);
}
