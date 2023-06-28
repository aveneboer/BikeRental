package nl.anouk.bikerental.exceptions;

public class BikeNotAvailableException extends RuntimeException {
    public BikeNotAvailableException(String message) {
        super(message);
    }
}