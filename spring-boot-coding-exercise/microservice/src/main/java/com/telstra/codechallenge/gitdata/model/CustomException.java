package com.telstra.codechallenge.gitdata.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
}
