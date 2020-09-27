package cd.wangyong.simple_pay;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cd.wangyong.simple_pay.adapter.PayChannel;
import cd.wangyong.simple_pay.enums.PayWayEnum;
import cd.wangyong.simple_pay.valobjs.PayDecision;
import cd.wangyong.simple_pay.valobjs.PayResult;


/**
 * @author andy
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    private PayDecisionMaker payDecisionMaker;

    @Resource(name = "balanceAccountAdapter")
    private PayChannel balanceAccountAdapter;

    @Resource(name = "aliPayAccountAdapter")
    private PayChannel aliPayAccountAdapter;

    @Override
    public PayResult pay(String requestId, int userId, BigDecimal amount) {
        PayDecision decision = payDecisionMaker.makeDecision(userId, amount);
        if (!decision.isSuccess()) {
            return PayResult.fail("支付决策失败，原因：" + decision.getMessage());
        }

        // 依次支付，只是反应业务逻辑本身，未从技术侧保证数据一致性，如果有一致性需求还需要加：1.反查接口；2.逆向回滚接口；3.异步保证任务；4.异常监控告警等补偿补救措施
        decision.getPayWayAmountContext().forEach((payWay, payWayAmount) -> {
            if (payWay == PayWayEnum.BALANCE) {
                balanceAccountAdapter.withhold(requestId, userId, payWayAmount);
            }
            else if (payWay == PayWayEnum.ALI_PAY) {
                aliPayAccountAdapter.withhold(requestId, userId, payWayAmount);
            }
        });
        return PayResult.success(decision.getPayWayAmountContext());
    }
}
