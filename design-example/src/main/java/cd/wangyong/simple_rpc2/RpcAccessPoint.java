package cd.wangyong.simple_rpc2;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

/**
 * RPC对外提供服务的接口（调用端、服务端两端）
 * @author andy
 * @since 2020/10/11
 */
public interface RpcAccessPoint extends Closeable {
    /**
     * 客户端获取远程服务的引用
     * @param uri 远程服务地址
     * @param serviceClass 服务接口Class
     * @param <T> 服务接口类型
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    /**
     * 服务端注册服务实现
     * @param service 实现实例
     * @param serviceClass 服务接口CLass
     * @param <T> 服务接口类型
     * @return 服务地址
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 服务端启动RPC框架，监听端口，开始提供远程服务
     * @return 服务实例，用于程序停止时安全关闭服务
     */
    Closeable startService() throws Exception;

    /**
     * 获取注册中心的引用
     * @param uri
     * @return
     */
    NameService getNameService(URI uri);
}
