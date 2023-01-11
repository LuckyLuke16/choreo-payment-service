package com.example.paymentservice.controller;

import com.example.paymentservice.model.ItemDetailDTO;
import com.example.paymentservice.model.paymentMethods.Method;
import com.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PaymentController implements PaymentOperations{

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Long fulfillPayment(String userId, String paymentMethod, List<ItemDetailDTO> itemsToPay) {
        try {
            Method method = Method.valueOf(paymentMethod);
            return this.paymentService.makePayment(userId, method, itemsToPay);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
