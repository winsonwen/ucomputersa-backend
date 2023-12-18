package ucomputersa.constant;


public enum OrderStatusEnum {

    SENDING_CONFIRMATION_EMAIL(1,"sending_confirmation_email"),


    WAITING_FOR_CALL(2,"waiting_for_call"),

    WAITING_FOR_PAYMENT(3,"waiting_for_payment"),

    WAITING_FOR_D2D_SERVICE(4,"waiting_for_d2d_service"),

    completed(5,"completed");

    ;
    private final int code;
    private final String msg;

    OrderStatusEnum(int code, String msg) {
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
