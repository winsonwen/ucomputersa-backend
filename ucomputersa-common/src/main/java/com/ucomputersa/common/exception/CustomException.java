package com.ucomputersa.common.exception;

public class CustomException extends RuntimeException {

    private final BizCodeEnume errorCode;

    public CustomException(BizCodeEnume bizCodeEnume) {
        super();
        this.errorCode = bizCodeEnume;
    }

    public String getErrorCode() {
        return "" + errorCode.getCode();
    }

    public String getMessage() {
        return errorCode.getMsg();
    }

}
