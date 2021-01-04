package cd.wangyong.concurrent_example.模式.Actor模式;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author andy
 * @since 2021/1/4
 */
public class CounterActor extends UntypedAbstractActor {
    private int counter = 0;

    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof Number) {
            counter += ((Number)message).intValue();
        }
        else {
            System.out.println(counter);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("HelloSystem");
        ActorRef counterActor = system.actorOf(Props.create(CounterActor.class));

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100000; j++) {
                    counterActor.tell(1, ActorRef.noSender());
                }
            });
        }

        executorService.shutdown();
        Thread.sleep(1000);
        counterActor.tell("", ActorRef.noSender());
    }
}
