package com.gil.easyjwt.exception;

public abstract class EasyJwtException extends RuntimeException {

    public EasyJwtException(String message) {
        super(message);
    }
}
