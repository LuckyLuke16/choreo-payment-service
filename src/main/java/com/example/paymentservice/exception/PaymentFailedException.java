package com.example.paymentservice.exception;

import com.example.paymentservice.model.paymentMethods.Method;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(Method paymentMethod) {
        super("Failed to pay for item with " + paymentMethod);
    }
}
