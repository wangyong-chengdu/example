package cd.wangyong.effective_java_example.设计模式.开闭原则;

import java.util.LinkedList;
import java.util.List;

/**
 * 按键
 * @author andy
 * @since 2021/1/7
 */
public class Button {
    private List<ButtonListener> listeners;

    public Button() {
        this.listeners = new LinkedList<>();
    }

    public void addListener(ButtonListener listener) {
        assert listener != null;
        listeners.add(listener);
    }

    public void press() {
        for (ButtonListener listener : listeners) {
            listener.buttonPressed();
        }
    }
}
