package cd.wangyong.effective_java_example.arraylist;

import java.util.ArrayList;

/**
 * @author andy
 * @since 2020/11/12
 */
public class ArrayListExample {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(null);
        System.out.println(list.size());
    }
}
