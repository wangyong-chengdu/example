package cd.wangyong.effective_java_example.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * @author andy
 * @since 2021/1/15
 */
public class 文件拷贝方式 {

    public static void copyFileByStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    /**
     * channel transferTo、
     * 基于NIO，数据拷贝过程不需要用户态参与，省去了上下文切换的开销和不必要的内存拷贝
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileByChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            for (long cnt = sourceChannel.size(); cnt > 0;) {
                long transferred = sourceChannel.transferTo(sourceChannel.position(), cnt, destChannel);
                sourceChannel.position(sourceChannel.position() + transferred);
                cnt -= transferred;
            }
        }

    }

//    public static void copyByAPI(File source, File dest) throws FileNotFoundException {
//        Files.copy(new FileInputStream(source), new FileOutputStream(dest));
//    }
}
