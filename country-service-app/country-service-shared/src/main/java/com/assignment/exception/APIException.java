package com.assignment.exception;

/**
 * Base exception class for all the custom exceptions.
 *
 */
public class APIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private Object[] messageArgs;

    public APIException(final String messageKey) {
        this.messageKey = messageKey;
    }

    public APIException(final ErrorCodes errorCodes) {
        this.messageKey = errorCodes.getErrorCode();
    }

    public APIException(final String messageKey, final Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

}
