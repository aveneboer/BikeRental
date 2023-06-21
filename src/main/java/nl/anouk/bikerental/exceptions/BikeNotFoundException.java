package nl.anouk.bikerental.exceptions;

public class BikeNotFoundException extends RuntimeException {
    public BikeNotFoundException(String message) {
        super(message);
    }
}