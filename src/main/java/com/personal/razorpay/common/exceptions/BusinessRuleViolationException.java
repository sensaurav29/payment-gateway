package com.personal.razorpay.common.exceptions;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends RuntimeException {

    private String errorCode;

    public BusinessRuleViolationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
