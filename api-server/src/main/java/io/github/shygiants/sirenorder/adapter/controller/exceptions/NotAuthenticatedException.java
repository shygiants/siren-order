package io.github.shygiants.sirenorder.adapter.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthenticatedException extends ResponseStatusException {

    public NotAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "Authentication failed");
    }
}
