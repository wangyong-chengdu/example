package cd.wangyong.proxy_example.proxy;

public class UserDao implements IUserDao {
    @Override
    public void save() {
        System.out.println("模拟：保存用户");
    }

    @Override
    public void find() {
        System.out.println("模拟：查找用户");
    }
}
