package cd.wangyong.effective_java_example.设计模式.构建型.建造者模式.构建资源池配置;

/**
 * @author andy
 * @since 2021/1/26
 */
public class ResourcePoolConfig {
    private final String name;
    private final int maxTotal;
    private final int maxIdle;
    private final int minIdle;

    private ResourcePoolConfig(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxIdle;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    public static class Builder {
        private static final int DEFAULT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;

        private String name;
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;

        public Builder setName(String name) {
            assert name != null;
            this.name = name;
            return this;
        }

        public Builder setMaxTotal(int maxTotal) {
            if (maxTotal <= 0) throw new IllegalArgumentException("...");
            this.maxTotal = maxTotal;
            return this;
        }

        /**
         * 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
         */
        public ResourcePoolConfig build() {
            if (name == null) {
                throw new IllegalArgumentException("...");
            }
            if (maxIdle > maxTotal) {
                throw new IllegalArgumentException("...");
            }
            if (minIdle > maxTotal || minIdle > maxIdle) {
                throw new IllegalArgumentException("...");
            }
            return new ResourcePoolConfig(this);
        }
    }
}
