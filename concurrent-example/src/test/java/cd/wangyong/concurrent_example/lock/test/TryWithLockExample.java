package cd.wangyong.concurrent_example.lock.test;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import cd.wangyong.concurrent_example.lock.try_with_lock.TryWithLock;

/**
 * @author andy
 * @since 2020/9/30
 */
public class TryWithLockExample {

    public static void main(String[] args) {
        while (true) {
            _do();
        }
    }

    private static void _do() {
        try (TryWithLock lock = new TryWithLock(new ReentrantLock())) {
            lock.lock();
            System.out.println("i come in.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
