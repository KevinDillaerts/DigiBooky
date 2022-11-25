package com.theexceptions.digibooky.API;

import com.theexceptions.digibooky.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({BookNotFoundException.class, BookAlreadyExistsException.class, BookAlreadyLentOutException.class, InvalidFilterValueException.class})
    protected void bookExceptionHandler(RuntimeException ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, EmailNotValidException.class, FieldIsEmptyException.class, MemberAlreadyExistsException.class, UserNotFoundException.class})
    protected void userExceptionHandlerBadRequest(RuntimeException ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class, WrongPasswordException.class})
    protected void userExceptionHandlerForbidden(RuntimeException ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
    }
}
