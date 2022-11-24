package com.theexceptions.digibooky.exceptions;

public class BookAlreadyLentOutException extends RuntimeException{
    public BookAlreadyLentOutException(String message) {
        super(message);
    }
}
