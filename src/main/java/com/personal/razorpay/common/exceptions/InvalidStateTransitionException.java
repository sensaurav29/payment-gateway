package com.personal.razorpay.common.exceptions;

public class InvalidStateTransitionException extends RuntimeException {

    public String fromState;
    public String toEvent;

    public InvalidStateTransitionException(String fromState, String toEvent) {
        super("Invalid state transition from " + fromState + " with event " + toEvent);
        this.fromState = fromState;
        this.toEvent = toEvent;
    }
}
