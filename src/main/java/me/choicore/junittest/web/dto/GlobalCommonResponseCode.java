package me.choicore.junittest.web.dto;

public enum GlobalCommonResponseCode {
    SUCCESS(0, "success"),
    FAIL(-1, "fail");

    private int code;
    private String message;

    GlobalCommonResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
