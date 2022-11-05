package com.softserve.util.rest;

import com.softserve.exceptions.ArticleException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ArticleAdviceException {

    @ExceptionHandler(ArticleException.class)
    public ResponseEntity<Map<String, Object>> handleError(ArticleException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("code", HttpStatus.BAD_REQUEST);
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
