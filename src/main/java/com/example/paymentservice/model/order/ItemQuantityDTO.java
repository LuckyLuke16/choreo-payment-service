package com.example.paymentservice.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemQuantityDTO {

    private HashMap<Integer, Integer> itemsFromShoppingCart;
}
