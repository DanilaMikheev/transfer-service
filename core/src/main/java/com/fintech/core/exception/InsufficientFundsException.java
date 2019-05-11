package com.fintech.core.exception;

/**
 * @author d.mikheev on 10.05.19
 */
public class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
