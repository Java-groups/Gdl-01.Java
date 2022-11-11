package com.softserve.security.handler;

import com.softserve.exceptions.ArticleException;
import com.softserve.exceptions.CustomException;
import com.softserve.util.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ArticleResponseException extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value
            = { ArticleException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex, Constants.BODY_ERROR_MESSAGE,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}