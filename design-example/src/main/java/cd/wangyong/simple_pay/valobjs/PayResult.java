package cd.wangyong.simple_pay.valobjs;


import java.math.BigDecimal;
import java.util.Map;

import cd.wangyong.simple_pay.enums.PayWayEnum;

/**
 * @author andy
 */
public class PayResult {
    private final boolean success;
    private final String message;
    private final Map<PayWayEnum, BigDecimal> payWayMap;

    private PayResult(boolean isSuccess, String message, Map<PayWayEnum, BigDecimal> payWayMap) {
        this.success = isSuccess;
        this.message = message;
        this.payWayMap = payWayMap;
    }

    public static PayResult success(Map<PayWayEnum, BigDecimal> payWayMap) {
        return new PayResult(true, null, payWayMap);
    }

    public static PayResult fail(String message) {
        return new PayResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<PayWayEnum, BigDecimal> getPayWayMap() {
        return payWayMap;
    }
}
