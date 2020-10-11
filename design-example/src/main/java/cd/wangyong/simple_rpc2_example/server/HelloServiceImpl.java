package cd.wangyong.simple_rpc2_example.server;

import cd.wangyong.simple_rpc2_example.HelloService;

/**
 * @author andy
 * @since 2020/10/11
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
