package com.ucomputersa.auth.constant;


public enum RegisterTypesEnum {
    PASSWORD(1, "password"),

    OATH2(10, "oath2"),
    BOTH_OATH2_PASSWORD(11,"bothOath2Password");

    private final int code;
    private final String msg;

    RegisterTypesEnum(int code, String msg) {
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
