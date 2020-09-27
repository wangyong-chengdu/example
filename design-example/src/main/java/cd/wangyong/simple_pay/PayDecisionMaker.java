package cd.wangyong.simple_pay;

import java.math.BigDecimal;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cd.wangyong.simple_pay.adapter.Account;
import cd.wangyong.simple_pay.enums.PayWayEnum;
import cd.wangyong.simple_pay.valobjs.PayDecision;

/**
 * 支付决策者
 *
 * @author andy
 */
@Component
public class PayDecisionMaker {
    @Resource
    private Account balanceAccountAdapter;
    @Resource
    private Account aliPayAccountAdapter;

    public PayDecision makeDecision(int userId, BigDecimal amount) {
        Assert.isTrue(amount != null && amount.compareTo(BigDecimal.ZERO) > 0, "支付总金额必须大于0");
        BigDecimal balance = balanceAccountAdapter.getBalance(userId);
        if (balance.compareTo(amount) >= 0) {
            return PayDecision.success().addPayWayAmount(PayWayEnum.BALANCE, amount);
        }

        BigDecimal aliPayBalance = aliPayAccountAdapter.getBalance(userId);
        if (balance.compareTo(BigDecimal.ZERO) == 0 && aliPayBalance.compareTo(amount) >= 0) {
            return PayDecision.success().addPayWayAmount(PayWayEnum.ALI_PAY, amount);
        }

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal remainder = amount.subtract(balance);
            if (aliPayBalance.compareTo(remainder) >= 0) {
                return PayDecision.success().addPayWayAmount(PayWayEnum.BALANCE, balance).addPayWayAmount(PayWayEnum.ALI_PAY, remainder);
            }
        }
        return PayDecision.fail("余额不足");
    }

}
