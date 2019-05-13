package com.fintech.core.exception;

/**
 * Exception if clientId not match with accFrom account owner
 *
 * @author d.mikheev on 10.05.19
 */
public class NoPermissionException extends Exception {

    public NoPermissionException(String message) {
        super(message);
    }
}
