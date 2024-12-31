package com.example.calculator;

public enum OPERATORS {
    PLUS("+"),
    MINUS("-"),
    DIVISION("/"),
    MULTIPLICATION("*"),
    EQUALS("="),
    SQRT("sqrt"),
    SQR("^");
    private final String code;
    OPERATORS(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OPERATORS fromCode(String code) {
        for (OPERATORS operator : values()) {
            if (operator.code.equals(code)) {
                return operator;
            }
        }
        return null;


}}
