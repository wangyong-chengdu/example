package cd.wangyong.effective_java_example.对象;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 对象引用
 * @author andy
 * @since 2021/1/13
 */
public class ReferenceExample {

    public static void main(String[] args) {
        Object counter = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();

        PhantomReference<Object> p = new PhantomReference<>(counter, referenceQueue);
        counter = null;
        System.gc();

        try {
            Reference<Object> ref = referenceQueue.remove(1000L);
            if (ref != null) {
                System.out.println(ref.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
