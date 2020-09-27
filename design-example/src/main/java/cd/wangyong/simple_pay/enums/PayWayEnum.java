package cd.wangyong.simple_pay.enums;

/**
 * @author andy
 */
public enum PayWayEnum {
    BALANCE(1, "账户余额"),
    ALI_PAY(2, "支付宝");

    PayWayEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * ID
     */
    private final int id;
    /**
     * 描述
     */
    private final String desc;

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
