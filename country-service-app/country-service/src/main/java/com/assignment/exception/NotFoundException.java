package com.assignment.exception;

/**
 * This exception indicates that there is no such entity present in DB or on the
 * external endpoint for the given identifier.
 *
 */
public class NotFoundException extends APIException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super(ErrorCodes.NotFound);
    }

    public NotFoundException(final String messageKey) {
        super(messageKey);
    }

    public NotFoundException(final ErrorCodes errorCodes) {
        super(errorCodes);
    }

    public NotFoundException(final String messageKey, final Object[] messageArgs) {
        super(messageKey, messageArgs);
    }

}
