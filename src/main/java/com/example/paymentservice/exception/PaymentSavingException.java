package com.example.paymentservice.exception;

public class PaymentSavingException extends RuntimeException {

    public PaymentSavingException() { super("Payment could not be saved");}
}
