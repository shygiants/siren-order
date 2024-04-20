package io.github.shygiants.sirenorder.adapter.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    public BadRequestException(Throwable cause) {
        super(HttpStatus.BAD_REQUEST, cause.getMessage(), cause);
    }
}
