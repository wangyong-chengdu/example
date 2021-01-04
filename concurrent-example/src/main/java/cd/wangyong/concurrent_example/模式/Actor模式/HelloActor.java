package cd.wangyong.concurrent_example.模式.Actor模式;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author andy
 * @since 2021/1/4
 */
public class HelloActor extends UntypedAbstractActor {
    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        System.out.println("Hello " + message);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("HelloSystem");
        ActorRef helloActor = system.actorOf(Props.create(HelloActor.class));

        helloActor.tell("Actor", ActorRef.noSender());
    }
}
