package cd.wangyong.concurrent_example.completable_future;

import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

/**
 * @author andy
 * @since 2020/10/15
 */
public class Client {
    @Resource
    private TransferService transferService;
    private final static int A = 1000;
    private static final int B = 1001;

    public void syncInvoke() throws ExecutionException, InterruptedException {
        transferService.transfer(A, B, 100).get();
        System.out.println("Transfer completed.");
    }

    public void asyncInvoke() {
        transferService.transfer(A, B, 100)
                .thenRun(() -> System.out.println("转账完成"));
    }


}
