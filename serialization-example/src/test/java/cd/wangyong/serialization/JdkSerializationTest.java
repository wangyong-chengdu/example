package cd.wangyong.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author andy
 * @since 2020/10/30
 */
public class JdkSerializationTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // jdk序列化对象
        String basePath = System.getProperty("user.home") + "/datas";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(basePath + "student.dat"));
        Student student = new Student(100, "Wang Yong");
        oos.writeObject(student);
        oos.flush();
        oos.close();

        // jdk反序列化对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(basePath + "student.dat"));
        Student readStudent = (Student) ois.readObject();
        ois.close();
        System.out.println(readStudent);
    }



}
