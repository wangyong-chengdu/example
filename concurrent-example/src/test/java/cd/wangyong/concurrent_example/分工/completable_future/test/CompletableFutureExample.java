package cd.wangyong.concurrent_example.分工.completable_future.test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andy
 * @since 2020/10/23
 */
@RunWith(JUnit4.class)
public class CompletableFutureExample {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureExample.class);

    @After
    public void doAfter() throws InterruptedException {
//        while (Thread.activeCount() > 0) {
//            Thread.yield();
//        }
        if (Thread.activeCount() > 0) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void testRunAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("run end ...");
        });
    }

    @Test
    public void testSupplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("run end ...");
            return System.currentTimeMillis();
        });

        System.out.println("Time:" + future.get());
    }

    // whenComplete 用于线程完成任务后立即执行下一个任务
    @Test
    public void testWhenComplete() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("任務1执行完成");
        });
        future.whenComplete((val, throwable) -> {
            System.out.println("任务2执行完成");
        });
    }

    @Test
    public void testWhenComplete2() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            throw new RuntimeException("任务1执行失败");
        });
        future.whenComplete((val, throwable) -> {
            logger.info("任务2执行完成, message:{}", throwable.getMessage());
        });
        future.exceptionally(throwable -> {
            logger.error("任务1执行失败，message:{}", throwable.getMessage());
            return null;
        });
    }

    // whenCompleteAsync和whenComplete差别是whenComplete后待执行任务是交给本线程执行还是交给线程池
    @Test
    public void testWhenCompleteAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            throw new RuntimeException("任务1执行失败");
        });
        future.whenCompleteAsync((val, throwable) -> {
            logger.info("任务2执行完成, message:{}", throwable.getMessage());
        });
        future.exceptionally(throwable -> {
            logger.error("任务1执行失败，message:{}", throwable.getMessage());
            return null;
        });
    }

    // thenApply用于一个线程依赖另一个线程时，可通过thenApply将两个线程串行化
    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            int result = new Random().nextInt(100);
            logger.info("result1 = {}", result);
            return result;
        }).thenApply(res -> {
            long result = res * 5;
            logger.info("result2 = {}", result);
            return result;
        });

        System.out.println("result = " + future.get());
    }

    @Test
    public void testThenApplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            int result = new Random().nextInt(100);
            logger.info("result1 = {}", result);
            return result;
        }).thenApplyAsync(res -> {
            long result = res * 5;
            logger.info("result2 = {}", result);
            return result;
        });

        System.out.println("result = " + future.get());
    }

    // handle 是执行任务完成时对结果的处理。
    // handle 方法和 thenApply 方法处理方式基本一样。不同的是 handle 是在任务完成后再执行，还可以处理异常的任务。thenApply 只可以执行正常的任务，任务出现异常则不执行 thenApply 方法。
    @Test
    public void testHandle() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            int result = new Random().nextInt(100);
            logger.info("result1 = {}", result);
            return result;
        }).handle((res, throwable) -> {
            long result = -1L;
            if (throwable != null) {
                logger.error("Task1 execute fail, e:{}", throwable);
            }
            else {
                result = res * 5;
                logger.info("result2 = {}", result);
            }
            return result;
        });

        System.out.println("result = " + future.get());
    }

    @Test
    public void testHandle2() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("任务1执行失败");
        }).handle((res, throwable) -> {
            long result = -1L;
            if (throwable != null) {
                logger.error("Task1 execute fail.", throwable);
            }
            else {
                result = (int)res * 5;
                logger.info("result2 = {}", result);
            }
            return result;
        });

        System.out.println("result = " + future.get());
    }

    // thenAccept用于接收任务的返回结果，并消费处理，无返回结果
    @Test
    public void testThenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            return new Random().nextInt(10);
        }).thenAccept(res -> {
            logger.info("Res:{}", res);
        });
        System.out.println(future.get());
    }

    @Test
    public void testThenAcceptAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> new Random().nextInt(10)).thenAcceptAsync(res -> {
            logger.info("Res:{}", res);
        });
        System.out.println(future.get());
    }

    // thenRun不关心任务的返回结果，只要上面的任务执行完毕，就开始执行thenRun
    @Test
    public void testThenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> new Random().nextInt(10)).thenRun(() -> logger.info("Then run"));
        future.get();
    }

    @Test
    public void testThenRunAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> new Random().nextInt(10)).thenRunAsync(() -> logger.info("Then run"));
        future.get();
    }

    // thenCombine合并任务：把两个CompletionStage任务都执行完毕后，将两个任务的结果一块交给thenCombine来处理
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<String> future = future1.thenCombine(future2, (res1, res2) -> res1 + " " + res2);
        logger.info("Res={}", future.get());
    }

    @Test
    public void testThenCombineAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<String> future = future1.thenCombineAsync(future2, (res1, res2) -> res1 + " " + res2);
        logger.info("Res={}", future.get());
    }

    // 当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗，没有返回结果
    @Test
    public void testThenAcceptBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.thenAcceptBoth(future2, (res1, res2) -> logger.info("Res={}", res1 + " " + res2));
        logger.info("Future={}", future.get());
    }

    @Test
    public void testThenAcceptBothAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.thenAcceptBothAsync(future2, (res1, res2) -> logger.info("Res={}", res1 + " " + res2));
        logger.info("Future={}", future.get());
    }

    // applyToEither方法：两个CompletionStage，谁执行的结果快，我就用哪个
    @Test
    public void testApplyToEither() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<String> future = future1.applyToEither(future2, (res) -> {
            logger.info("Res={}", res);
            return "apply res =" + res;
        });
        logger.info(future.get());
    }

    @Test
    public void testApplyToEitherAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<String> future = future1.applyToEitherAsync(future2, (res) -> {
            logger.info("Res={}", res);
            return "apply res =" + res;
        });
        logger.info(future.get());
    }

    // acceptEither方法： 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的消耗操作。
    @Test
    public void testAcceptEither() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.acceptEither(future2, (res) -> logger.info("Res={}", res));
        future.get();
    }

    @Test
    public void testAcceptEitherAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.acceptEitherAsync(future2, (res) -> logger.info("Res={}", res));
        future.get();
    }

    // runAfterEither: 两个CompletionStage，任何一个执行完了都会触发下一步操作
    @Test
    public void testRunAfterEither() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.runAfterEither(future2, () -> logger.info("Done."));
        future.get();
    }

    @Test
    public void testRunAfterEitherAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.runAfterEitherAsync(future2, () -> logger.info("Done."));
        future.get();
    }

    // runAfterBoth 两个CompletionStage，都执行完了才会执行下一步操作
    @Test
    public void testRunAfterBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.runAfterBoth(future2, () -> logger.info("Done."));
        future.get();
    }

    // runAfterBoth 两个CompletionStage，都执行完了才会执行下一步操作
    @Test
    public void testRunAfterBothAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");

        CompletableFuture<Void> future = future1.runAfterBothAsync(future2, () -> logger.info("Done."));
        future.get();
    }

    // thenCompose方法：允许你对来两个CompletionStage进行流水线操作，第一个操作完毕后，将其结果作为参数传递给第二个
    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = future1.thenCompose(res -> CompletableFuture.supplyAsync(() -> res + " world."));
        logger.info("Then Compose result:{}", future2.get());
    }

    @Test
    public void testThenComposeAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> future2 = future1.thenComposeAsync(res -> CompletableFuture.supplyAsync(() -> res + " world."));
        logger.info("Then Compose result:{}", future2.get());
    }

}


