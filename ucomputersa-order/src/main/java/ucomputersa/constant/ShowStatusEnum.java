package ucomputersa.constant;


public enum ShowStatusEnum {

    NOT_SHOWED(0,"not_showed"),

    SHOWED(1,"showed");

    private final int code;
    private final String msg;

    ShowStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
