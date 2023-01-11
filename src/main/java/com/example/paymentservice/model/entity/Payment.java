package com.example.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long paymentId;

    private String userId;

    private float price;

    private String paymentMethod;

    public Payment(String userId, float price, String paymentMethod) {
        this.userId = userId;
        this.price = price;
        this.paymentMethod = paymentMethod;
    }

}
