package cd.wangyong.serialization;

import java.io.Serializable;

/**
 * @author andy
 * @since 2020/10/31
 */
public class Student implements Serializable {
    private int no;
    private String name;

    public Student(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
