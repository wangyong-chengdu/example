package cd.wangyong.zk_example.Watch例子;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行器容器
 * @author andy
 * @since 2021/2/6
 */
public class Executor implements Watcher, Runnable, DataMonitorListener {
    private static final Logger logger = LoggerFactory.getLogger(Executor.class);

    private final DataMonitor dm;
    private final ZooKeeper zk;
    private final String fileName;
    private final String[] exec;

    private String zNode;
    private Process child;

    public Executor(String hostPort, String zNode, String fileName, String[] exec) throws KeeperException, IOException {
        this.fileName = fileName;
        this.exec = exec;
        this.zk = new ZooKeeper(hostPort, 3000, this);
        this.dm = new DataMonitor(zk, zNode, null, this);
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("USAGE: Executor hostPort znode filename program [args ...]");
            System.exit(2);
        }

        String hostPort = args[0];
        String zNode = args[1];
        String fileName = args[2];
        String[] exec = new String[args.length - 3];

        try {
            new Executor(hostPort, zNode, fileName, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.isDead()) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        public StreamWriter(InputStream is, OutputStream os) {
            this.os = os;
            this.is = is;
            start();
        }

        @Override
        public void run() {
            byte[] b = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                logger.info("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }
            child = null;
        }
        else {

            if (child != null) {
                logger.info("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                logger.error("write file:{} error", fileName, e);
            }

            try {
                logger.info("Starting child");
                child = Runtime.getRuntime().exec(this.exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                logger.error("Starting child error", e);
            }

        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }
}
