package com.softserve.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private String message;

    private int statusCode;

    public CustomException(String message){
        super(message);
        this.message = message;
    }
}
