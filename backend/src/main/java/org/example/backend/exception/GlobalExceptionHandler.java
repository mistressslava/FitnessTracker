package org.example.backend.exception;

import org.example.backend.planGenerator.ChatGPTRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlerNoSuchElementException(NoSuchElementException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmptyExerciseFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmptyExercise(EmptyExerciseFieldException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ChatGPTRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleChat(ChatGPTRequestException e) { return e.getMessage(); }

}
