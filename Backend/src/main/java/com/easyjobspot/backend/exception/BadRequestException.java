package com.easyjobspot.backend.exception;

class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}