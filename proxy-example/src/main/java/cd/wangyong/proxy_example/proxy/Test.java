package cd.wangyong.proxy_example.proxy;

public class Test {

    public static void main2(String[] args) {
        IUserDao proxy = new UserDaoProxy();
        proxy.save();
    }

    public static void main(String[] args) {
        IUserDao target = new UserDao();
        IUserDao proxy = (IUserDao) new ProxyFactory(target).getProxyInstance();
        System.out.println(proxy.getClass());
        proxy.save();
    }
}
