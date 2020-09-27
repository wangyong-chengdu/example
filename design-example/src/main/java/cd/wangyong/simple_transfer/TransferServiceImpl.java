package cd.wangyong.simple_transfer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;

public class TransferServiceImpl implements TransferService{
    @Autowired
    private AccountService accountService;

    @Override
    public CompletableFuture<Void> transfer(int fromAccount, int toAccount, int amount) {
        return accountService.add(fromAccount, -1 * amount)
//                .exceptionally((t, v) -> {
//                    // 1.检查是否需要补偿；
//                    // 2.补偿
//                })
                .thenCompose(v -> accountService.add(toAccount, amount));
    }
}
