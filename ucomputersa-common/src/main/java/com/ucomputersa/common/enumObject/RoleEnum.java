package com.ucomputersa.common.enumObject;


public enum RoleEnum {

    REGULAR_MEMBER(1, "regular_member"),
    ADMIN(2, "admin");

    private final int code;
    private final String msg;

    RoleEnum(int code, String msg) {
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
