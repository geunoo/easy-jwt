package com.gil.easyjwt.exception;

public class ExpiredTokenException extends EasyJwtException {
    public ExpiredTokenException() {
        super("Token Expired");

    }
}
