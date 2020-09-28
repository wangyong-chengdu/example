package cd.wangyong.simple_rpc.protocol;

/**
 * 包类型：请求 or 响应
 * @author andy
 * @since 2020/9/28
 */
public enum PackageType {
    REQUEST((byte) 0, "Request"),
    RESPONSE((byte) 1, "Response");

    private byte code;
    private String name;

    PackageType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
