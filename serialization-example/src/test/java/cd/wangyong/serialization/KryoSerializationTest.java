package cd.wangyong.serialization;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializationTest {
    private Kryo kryo;
    private Output output;
    private Input input;

    @Before
    public void init() throws FileNotFoundException {
        kryo = new Kryo();
        output = new Output(new FileOutputStream("/home/andy/test_data/file.dat"));
        input = new Input(new FileInputStream("/home/andy/test_data/file.dat"));
    }

    @Test
    public void serializingObjectAndRead() {
        Object object = "Hello, My name is wangyong.";

        kryo.writeClassAndObject(output, object);
        output.close();

        Object theObject = kryo.readClassAndObject(input);
        input.close();
        Assert.assertEquals(theObject, "Hello, My name is wangyong.");
    }

    @Test
    public void test() {
        String someString = "Multiple Objects";
        Date someDate = new Date(915170400000L);

        kryo.writeObject(output, someString);
        kryo.writeObject(output, someDate);
        output.close();

        String readString = kryo.readObject(input, String.class);
        Date readDate = kryo.readObject(input, Date.class);
        input.close();

        Assert.assertEquals(readString, "Multiple Objects");
        Assert.assertEquals(readDate.getTime(), 915170400000L);
    }

    @Test
    public void serializingPersonObject() {
        Person person = new Person();

        kryo.writeObject(output, person);
        output.close();

        Person readPerson = kryo.readObject(input, Person.class);
        input.close();

        Assert.assertEquals(readPerson.getName(), "John Doe");
    }

    @Test
    public void serializingPersonObject2() {
        kryo.register(Person.class);
        kryo.writeObject(output, new Person());
        output.close();

        Person readPerson = kryo.readObject(input, Person.class);
        input.close();

        Assert.assertEquals(readPerson.getName(), "John Doe");
    }

    public static class Person {
        private String name = "John Doe";
        private int age = 18;
        private Date birthDate = new Date(933191282821L);

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }
    }
}
