package com.assignment.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignment.dto.APIResponse;

/**
 * Exception handler class for all {@link APIException}. This handler class is
 * responsible for setting the locale specific messages for the output error
 * message.
 */
@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public APIExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InvalidInputException.class)
    public final ResponseEntity<APIResponse> handleInvalidInputException(final InvalidInputException ex, final WebRequest request) {

        final String msg = this.getLocalizedMessage(ex.getMessageKey(), ex.getMessageArgs());

        final APIResponse response = new APIResponse(false, msg);
        return new ResponseEntity<APIResponse>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<APIResponse> handleNotFoundException(final NotFoundException ex, final WebRequest request) {

        final String msg = this.getLocalizedMessage(ex.getMessageKey(), ex.getMessageArgs());

        final APIResponse response = new APIResponse(false, msg);
        return new ResponseEntity<APIResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<APIResponse> handleAllGenericException(final Exception ex, final WebRequest request) {
        final APIResponse response = new APIResponse(false, "Unable to process the request. Please try after sometime.");
        return new ResponseEntity<APIResponse>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Get the locale specific message. If Accept-Language header is present,
     * the locale will be set to satisfy the user asked language, if the
     * application supports it.
     */
    private String getLocalizedMessage(final String messageKey, final Object[] args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }
}
