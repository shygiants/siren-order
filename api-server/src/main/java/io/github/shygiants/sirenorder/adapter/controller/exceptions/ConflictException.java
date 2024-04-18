package io.github.shygiants.sirenorder.adapter.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public ConflictException(Throwable cause) {
        super(HttpStatus.CONFLICT, cause.getMessage(), cause);
    }
}
