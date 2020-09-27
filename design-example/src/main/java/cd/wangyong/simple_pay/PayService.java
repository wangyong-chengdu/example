package cd.wangyong.simple_pay;

import java.math.BigDecimal;

import cd.wangyong.simple_pay.valobjs.PayResult;

/**
 * @author andy
 */
public interface PayService {
    /**
     * 支付
     * @param requestId 请求ID
     * @param userId 用户ID
     * @param amount 支付金额
     * @return 支付结果
     */
    PayResult pay(String requestId, int userId, BigDecimal amount);
}
