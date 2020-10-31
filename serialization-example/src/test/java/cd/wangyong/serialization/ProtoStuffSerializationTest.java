package cd.wangyong.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author andy
 * @since 2020/10/31
 */
public class ProtoStuffSerializationTest {

    public static void main(String[] args) throws IOException {
        String basePath = System.getProperty("user.home") + "/datas";
        FileOutputStream fos = new FileOutputStream(basePath + "student_protostuff.dat");
        Student student = new Student(100, "Wang Yong");

        Schema<Student> schema = RuntimeSchema.getSchema(Student.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        ProtostuffIOUtil.writeTo(fos, student, schema, buffer);

        FileInputStream fis = new FileInputStream(basePath + "student_protostuff.dat");
        Student readStudent = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(fis, readStudent, schema);
        System.out.println(readStudent);
    }
}
