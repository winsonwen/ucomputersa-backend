package com.ucomputersa.monolithic.constant;


import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleEnum {

    @JsonProperty("REGULAR_USER")
    REGULAR_USER(1, "regular_user"),

    @JsonProperty("ADMIN")
    ADMIN(2, "admin"),

    @JsonEnumDefaultValue  // Default when unknown value is provided
    UNKNOWN(0, "unknown");

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
