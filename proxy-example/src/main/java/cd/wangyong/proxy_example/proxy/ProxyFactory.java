package cd.wangyong.proxy_example.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    private Object target;
    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
            String methodName = method.getName();
            if (methodName.equals("find")) return method.invoke(target, args);
            else {
                System.out.println("开启事务");
                Object result = method.invoke(target, args);
                System.out.println("提交事务");
                return result;
            }
        });
    }
}
