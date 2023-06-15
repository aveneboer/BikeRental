package nl.anouk.bikerental.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public BadRequestException() {
        super();
    }
    public BadRequestException(String message) {
        super(message);
    }
}