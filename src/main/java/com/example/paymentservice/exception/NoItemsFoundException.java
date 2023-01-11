package com.example.paymentservice.exception;

public class NoItemsFoundException extends RuntimeException{

    public NoItemsFoundException() {
        super("No items found");
    }
}
