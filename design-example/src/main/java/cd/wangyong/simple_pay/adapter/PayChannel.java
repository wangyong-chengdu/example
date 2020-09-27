package cd.wangyong.simple_pay.adapter;

import java.math.BigDecimal;

import cd.wangyong.simple_pay.valobjs.WithholdInfo;


/**
 * @author andy
 */
public interface PayChannel {
    /**
     * 扣款
     * @param requestId 请求ID
     * @param userId 用户ID
     * @param amount 金额
     */
    void withhold(String requestId, int userId, BigDecimal amount);

    /**
     * 回滚
     * @param requestId 请求ID
     * @return true:回滚成功 false:回滚失败
     */
    boolean rollback(String requestId);

    /**
     * 根据requestId反查
     * @param requestId 请求ID
     * @return 扣款详情
     */
    WithholdInfo getWithholdInfo(String requestId);
}
