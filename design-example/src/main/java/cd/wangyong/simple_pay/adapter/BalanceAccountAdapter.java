package cd.wangyong.simple_pay.adapter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import cd.wangyong.simple_pay.valobjs.WithholdInfo;


/**
 * @author andy
 */
@Component
public class BalanceAccountAdapter implements PayChannel, Account {
    @Override
    public BigDecimal getBalance(int userId) {
        // TODO: 2020/8/20
        return null;
    }

    @Override
    public void withhold(String requestId, int userId, BigDecimal amount) {
        // TODO: 2020/8/20  
    }

    @Override
    public boolean rollback(String requestId) {
        // TODO: 2020/8/20  
        return false;
    }

    @Override
    public WithholdInfo getWithholdInfo(String requestId) {
        // TODO: 2020/8/20
        return null;
    }
}
