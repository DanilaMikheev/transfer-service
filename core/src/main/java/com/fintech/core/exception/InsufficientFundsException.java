package com.fintech.core.exception;

/**
 * Exception if amount greater than balance accFrom account number
 *
 * @author d.mikheev on 10.05.19
 */
public class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
