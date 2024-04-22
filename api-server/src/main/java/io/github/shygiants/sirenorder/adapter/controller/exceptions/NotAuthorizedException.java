package io.github.shygiants.sirenorder.adapter.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthorizedException extends ResponseStatusException {

    public NotAuthorizedException(Throwable cause) {
        super(HttpStatus.FORBIDDEN, cause.getMessage(), cause);
    }
}
