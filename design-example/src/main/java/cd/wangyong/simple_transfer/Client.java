package cd.wangyong.simple_transfer;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

public class Client {
    private static final int ACCOUNT_1 = 1000;
    private static final int ACCOUNT_2 = 1001;

    @Autowired
    private TransferService transferService;

    public void syncInvoke() throws ExecutionException, InterruptedException {
        transferService.transfer(ACCOUNT_1, ACCOUNT_2, 100).get();
        System.out.println("transfer completed!");
    }

    public void asyncInvoke() {
        transferService.transfer(ACCOUNT_1, ACCOUNT_2, 100)
                .thenRun(() -> System.out.println("transfer completed!"));
    }

}
