package cd.wangyong.proxy_example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("aspectTest.xml");
        TestBean testBean = appContext.getBean("test", TestBean.class);
        testBean.test();
    }
}
