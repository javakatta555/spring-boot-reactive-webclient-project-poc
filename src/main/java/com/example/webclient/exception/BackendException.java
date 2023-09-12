package com.example.webclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BackendException extends ResponseStatusException {
    private String messageKey;
    private Object[] messageParams;
    private ErrorResponse errorResponse;

    private boolean isThirdPartyError;

    public BackendException(HttpStatus status) {
        super(status);
    }

    public BackendException(HttpStatus status, String messageKey) {
        super(status, messageKey);
        this.messageKey = messageKey;
    }

    public BackendException(HttpStatus status, String messageKey, boolean isThirdPartyError, ErrorResponse errorResponse) {
        super(status, messageKey);
        this.messageKey = messageKey;
        this.errorResponse = errorResponse;
        this.isThirdPartyError = isThirdPartyError;
    }

    public BackendException(HttpStatus status, String messageKey, boolean isThirdPartyError, Object... params) {
        super(status, messageKey);
        this.messageKey = messageKey;
        this.messageParams = params;
        this.isThirdPartyError = isThirdPartyError;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public Object[] getMessageParams() {
        return messageParams;
    }

    public boolean isThirdPartyError() {
        return isThirdPartyError;
    }

    public void setThirdPartyError(boolean thirdPartyError) {
        isThirdPartyError = thirdPartyError;
    }
}
