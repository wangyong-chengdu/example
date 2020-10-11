package cd.wangyong.simple_rpc2_example.server;

import java.io.Closeable;
import java.io.File;
import java.net.URI;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cd.wangyong.simple_rpc2.NameService;
import cd.wangyong.simple_rpc2.RpcAccessPoint;
import cd.wangyong.simple_rpc2_example.HelloService;

/**
 * @author andy
 * @since 2020/10/11
 */
public class ServerBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);


    public static void main(String[] args) throws Exception {
        File nameServiceData = new File(new File(System.getProperty("java.io.tmpdir")), "cd.wangyong.simple_rpc2_name_service.data");
        String serviceName = HelloService.class.getCanonicalName();

        // 启动RPC框架、增加服务提供者
        logger.info("创建并启动RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceLoader.load(RpcAccessPoint.class).iterator().next();
             Closeable ignored = rpcAccessPoint.startService()) {
            NameService nameService = rpcAccessPoint.getNameService(nameServiceData.toURI());
            assert nameService != null;

            // 注册服务
            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            URI uri = rpcAccessPoint.addServiceProvider(new HelloServiceImpl(), HelloService.class);

            logger.info("服务名: {}, 向NameService注册...", serviceName);
            nameService.registerService(serviceName, uri);

            logger.info("开始提供服务，按任何键退出.");
            System.in.read();
            logger.info("Bye!");
        }

    }
}
