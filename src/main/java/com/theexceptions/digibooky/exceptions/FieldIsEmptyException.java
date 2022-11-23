package com.theexceptions.digibooky.exceptions;

public class FieldIsEmptyException extends RuntimeException {
    public FieldIsEmptyException(String message) {
        super(message);
    }
}
