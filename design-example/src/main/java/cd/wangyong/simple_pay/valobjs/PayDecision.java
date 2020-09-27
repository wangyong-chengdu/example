package cd.wangyong.simple_pay.valobjs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cd.wangyong.simple_pay.enums.PayWayEnum;

/**
 * @author andy
 */
public class PayDecision {
    private final boolean success;
    private final Map<PayWayEnum, BigDecimal> payWayAmountContext = new HashMap<>();
    private final String message;

    private PayDecision(boolean isSuccess, String message) {
        this.success = isSuccess;
        this.message = message;
    }

    public static PayDecision success() {
        return new PayDecision(true, null);
    }

    public static PayDecision fail(String message) {
        return new PayDecision(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public PayDecision addPayWayAmount(PayWayEnum payWayEnum, BigDecimal amount) {
        this.payWayAmountContext.put(payWayEnum, amount);
        return this;
    }

    public Map<PayWayEnum, BigDecimal> getPayWayAmountContext() {
        return payWayAmountContext;
    }

    public String getMessage() {
        return message;
    }
}
