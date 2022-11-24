package com.theexceptions.digibooky.exceptions;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
