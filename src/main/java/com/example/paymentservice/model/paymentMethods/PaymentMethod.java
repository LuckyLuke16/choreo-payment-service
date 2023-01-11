package com.example.paymentservice.model.paymentMethods;

public interface PaymentMethod {

    // logic for transactions
    void makeTransaction(float totalPrice);
}
