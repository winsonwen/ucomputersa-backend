package ucomputersa.constant;

public enum ServiceTypeEnum {

    DOOR_TO_DOOR(0,"d2d"),
    TO_STORE(1,"to store");

    private final int code;
    private final String msg;

    ServiceTypeEnum(int code, String msg) {
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
