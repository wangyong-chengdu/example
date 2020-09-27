package cd.wangyong.proxy_example.proxy;

public class UserDaoProxy implements IUserDao {
    private IUserDao target = new UserDao();

    @Override
    public void save() {
        System.out.println("代理操作：开启事务");
        target.save();
        System.out.println("代理操作：提交事务");
    }

    @Override
    public void find() {
        target.find();
    }
}
