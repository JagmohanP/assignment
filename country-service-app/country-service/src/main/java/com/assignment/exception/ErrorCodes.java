package com.assignment.exception;

/**
 * Enum class to hold all the error codes. The message.properties file have
 * detailed message for the error codes.
 *
 */
public enum ErrorCodes {

    InvalidInputData ("invalid.input.data"),
    NotFound ("not.found"),
    ExternalCountryDetailsServerError ("external.countrydetails.server.error"),
    UnsupportedCountryCode ("unsupported.countrycode");

    private String errorCode;

    private ErrorCodes(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
