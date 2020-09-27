package cd.wangyong.simple_pay.adapter;

import java.math.BigDecimal;

/**
 * @author andy
 */
public interface Account {
    /**
     * 账户余额查询
     * @param userId 用户ID
     * @return 余额
     */
    BigDecimal getBalance(int userId);
}
