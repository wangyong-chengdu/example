package cd.wangyong.concurrent_example.分工.优雅的终止线程;

/**
 * 日志收集监控代理
 * @author andy
 * @since 2020/12/30
 */
public class Proxy {
    private volatile boolean terminated = false;
    private boolean started = false;
    /**
     * 采集线程
     */
    Thread rptThread;

    public synchronized void start() {
        if (started) {
            return;
        }

        started = true;
        rptThread = new Thread(() -> {
//            while (!Thread.currentThread().isInterrupted()) { // 通过中断标识位不靠谱，任务里边我们没办法保证正确处理了中断标识位
            while (!terminated) {
                // TODO: 2020/12/30 采集、回传
                report();

                // 每隔2s进行采集回传数据
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            started = false;
        });

        rptThread.start();
    }

    public synchronized void stop() {
        terminated = true;
        rptThread.interrupt();
    }

    private void report() {


    }


}
