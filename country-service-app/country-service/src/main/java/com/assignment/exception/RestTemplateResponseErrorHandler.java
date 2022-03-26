package com.assignment.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Error handler class for the calls made by RestTemplate
 */
@Component
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(final ClientHttpResponse httpResponse)
            throws IOException {

        return ((HttpStatus.Series.CLIENT_ERROR == httpResponse.getStatusCode().series())
                || (HttpStatus.Series.SERVER_ERROR == httpResponse.getStatusCode().series()));
    }

    @Override
    public void handleError(final ClientHttpResponse httpResponse)
            throws IOException {

        if (HttpStatus.Series.SERVER_ERROR == httpResponse.getStatusCode().series()) {
            if (HttpStatus.INTERNAL_SERVER_ERROR == httpResponse.getStatusCode()) {
                throw new APIException(ErrorCodes.ExternalCountryDetailsServerError);
            }
        }
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException();
            }
        }
    }
}
