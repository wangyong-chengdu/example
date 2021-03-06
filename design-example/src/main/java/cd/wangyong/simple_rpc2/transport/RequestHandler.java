package cd.wangyong.simple_rpc2.transport;

import cd.wangyong.simple_rpc2.transport.command.Command;

/**
 * 请求处理器
 * @author andy
 * @since 2020/10/13
 */
public interface RequestHandler {

    /**
     * 处理请求
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     * @return
     */
    int type();

}
