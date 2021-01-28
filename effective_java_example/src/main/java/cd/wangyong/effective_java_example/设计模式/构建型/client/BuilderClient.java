package cd.wangyong.effective_java_example.设计模式.构建型.client;

import cd.wangyong.effective_java_example.设计模式.构建型.建造者模式.构建资源池配置.ResourcePoolConfig;

/**
 * @author andy
 * @since 2021/1/26
 */
public class BuilderClient {

    public static void main(String[] args) {
        ResourcePoolConfig resourcePoolConfig = new ResourcePoolConfig.Builder()
                .setName("object-pool")
                .setMaxTotal(16)
                .build();

    }
}
