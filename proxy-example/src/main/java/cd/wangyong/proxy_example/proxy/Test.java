package cd.wangyong.proxy_example.proxy;

public class Test {

    public static void main2(String[] args) {
        IUserDao proxy = new UserDaoProxy();
        proxy.save();
    }

    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        IUserDao target = new UserDao(); // 代理实例
        IUserDao proxy = (IUserDao) new ProxyFactory(target).getProxyInstance();
        System.out.println(proxy.getClass());
        proxy.save();
    }
}
