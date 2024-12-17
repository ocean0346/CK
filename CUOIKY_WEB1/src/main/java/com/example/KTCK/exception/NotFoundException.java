package com.example.KTCK.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id){
        super("Record not found with id: " + id);
    }
}
