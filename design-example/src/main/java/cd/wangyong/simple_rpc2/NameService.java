package cd.wangyong.simple_rpc2;

import java.io.IOException;
import java.net.URI;

/**
 * 名字服务（即注册中心）
 * @author andy
 * @since 2020/10/11
 */
public interface NameService {

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param uri 服务地址
     */
    void registerService(String serviceName, URI uri) throws IOException;

    /**
     * 查找服务（即服务发现）
     * @param serviceName 服务名称
     * @return 服务地址
     */
    URI lookupService(String serviceName) throws IOException;

}
