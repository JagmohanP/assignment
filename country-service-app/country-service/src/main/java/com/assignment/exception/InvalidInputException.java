package com.assignment.exception;

/**
 * This exception indicates that the received input data is invalid and user
 * should try the request again after correcting the input data.
 *
 */
public class InvalidInputException extends APIException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(final String messageKey) {
        super(messageKey);
    }

    public InvalidInputException(final ErrorCodes errorCodes) {
        super(errorCodes);
    }

    public InvalidInputException(final String messageKey, final Object[] messageArgs) {
        super(messageKey, messageArgs);
    }
}
