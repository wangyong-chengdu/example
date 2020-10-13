package cd.wangyong.simple_rpc2.transport;

import java.util.concurrent.CompletableFuture;

import cd.wangyong.simple_rpc2.transport.command.Command;

/**
 * @author andy
 * @since 2020/10/12
 */
public interface Transport {

    /**
     * 发送请求命令
     * @param request 请求命令
     * @return Future
     */
    CompletableFuture<Command> send(Command request);
}
