package cd.wangyong.simple_transfer;

import java.util.concurrent.CompletableFuture;

public interface TransferService {

    /***
     * 异步转账服务
     * @param fromAccount 转出账户
     * @param toAccount 转入账户
     * @param amount 转账金额
     * @return
     */
    CompletableFuture<Void> transfer(int fromAccount, int toAccount, int amount);
}
