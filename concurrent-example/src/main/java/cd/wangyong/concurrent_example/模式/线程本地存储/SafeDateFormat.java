package cd.wangyong.concurrent_example.模式.线程本地存储;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author andy
 * @since 2020/12/27
 */
public class SafeDateFormat {

    private static final ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static DateFormat get() {
        return threadLocal.get();
    }

    public static void main(String[] args) {
        DateFormat dateFormat = SafeDateFormat.get();
        System.out.println(dateFormat.format(new Date()));
    }
}
