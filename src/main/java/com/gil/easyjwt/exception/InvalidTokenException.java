package com.gil.easyjwt.exception;

public class InvalidTokenException extends EasyJwtException {
    public InvalidTokenException() {
        super("Invalid Token. Check your token");
    }
}
