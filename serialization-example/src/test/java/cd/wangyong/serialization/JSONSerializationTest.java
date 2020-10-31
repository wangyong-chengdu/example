package cd.wangyong.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSON;
/**
 * @author andy
 * @since 2020/10/31
 */
public class JSONSerializationTest {

    public static void main(String[] args) throws IOException {
        String basePath = System.getProperty("user.home") + "/datas";
        FileOutputStream fos = new FileOutputStream(basePath + "student_json.dat");
        Student student = new Student(100, "Wang Yong");

        JSON.writeJSONString(fos, StandardCharsets.UTF_8, student);

        FileInputStream fis = new FileInputStream(basePath + "student_json.dat");
        Student readStudent = JSON.parseObject(fis, StandardCharsets.UTF_8, Student.class);
        System.out.println(readStudent);
    }
}
