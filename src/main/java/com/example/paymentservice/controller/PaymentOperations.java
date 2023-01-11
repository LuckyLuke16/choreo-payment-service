package com.example.paymentservice.controller;

import com.example.paymentservice.model.ItemDetailDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/payments")
public interface PaymentOperations {

    @PostMapping
    Long fulfillPayment(@RequestParam String userId, @RequestParam String paymentMethod, @RequestBody List<ItemDetailDTO> itemsToPay);

}
