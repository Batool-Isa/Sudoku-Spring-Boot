package com.ga.sudoku.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBoardException extends RuntimeException {
    public InvalidBoardException(String msg) {
        super(msg);
    }
}