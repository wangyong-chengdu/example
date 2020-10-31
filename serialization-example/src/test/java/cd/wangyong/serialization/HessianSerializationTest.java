package cd.wangyong.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * @author andy
 * @since 2020/10/31
 */
public class HessianSerializationTest {

    public static void main(String[] args) throws IOException {
        String basePath = System.getProperty("user.home") + "/datas";
        FileOutputStream fos = new FileOutputStream(basePath + "student_hessian.dat");
        Student student = new Student(100, "Wang Yong");

        Hessian2Output output = new Hessian2Output(fos);
        output.writeObject(student);
        output.flush();
        output.close();

        Hessian2Input input = new Hessian2Input(new FileInputStream(basePath + "student_hessian.dat"));
        Student readStudent = (Student) input.readObject();
        input.close();
        System.out.println(readStudent);
    }

}
