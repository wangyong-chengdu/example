package cd.wangyong.simple_pay.valobjs;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 扣款信息
 * @author andy
 */
public class WithholdInfo {
    /**
     * 流水号
     */
    private String ticketId;
    /**
     * 扣款金额
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    private Date created;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
