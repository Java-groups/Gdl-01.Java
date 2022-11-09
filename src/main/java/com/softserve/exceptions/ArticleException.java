package com.softserve.exceptions;

import lombok.Data;

@Data
public class ArticleException extends RuntimeException{
    private String message;

    public ArticleException(String message){
        super(message);
        this.message =message;
    }
}
