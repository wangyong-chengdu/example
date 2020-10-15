package cd.wangyong.concurrent_example.completable_future;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

/**
 * @author andy
 * @since 2020/10/15
 */
public class TransferServiceImpl implements TransferService {
    @Resource
    private AccountService accountService;

    @Override
    public CompletableFuture<Void> transfer(int fromAccount, int toAccount, int amount) {
        return accountService.add(fromAccount, -1 * amount)
                .thenCompose(v -> accountService.add(toAccount, amount));
    }
}
