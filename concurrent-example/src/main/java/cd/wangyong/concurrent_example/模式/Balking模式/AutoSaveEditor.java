package cd.wangyong.concurrent_example.模式.Balking模式;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自动保存编辑器
 * @author andy
 * @since 2020/12/30
 */
public class AutoSaveEditor {
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean changed = false;

    /**
     * 自动保存
     */
    public void invokeAutoSave() {
        // 使用ScheduleWithFixedDelay()调度能保证同一时刻只有一个线程执行autoSave,因此不存在原子性问题
        scheduledExecutorService.scheduleWithFixedDelay(() -> autoSave(), 5, 5, TimeUnit.SECONDS);
    }

    // 确定是否存在竞态条件（使用ScheduleWithFixedDelay()调度能保证同一时刻只有一个线程执行autoSave,因此不存在原子性问题）：
    // 若存在竞态条件，则需要上锁
    // 若不存在竞态条件，则只需要保证条件的可见性
    private void autoSave() {
        if (!changed) {
            return;
        }
        changed = false;
        save();
    }

    /**
     * 保存
     */
    public void save() {

    }

    /**
     * 编辑
     */
    public void edit() {
        // TODO: 2020/12/30 do edit
        changed = true;
    }
}
