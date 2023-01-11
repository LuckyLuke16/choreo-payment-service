package com.example.paymentservice.model.order;

import com.example.paymentservice.model.ItemDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String paymentMethod;

    private Address address;

    private ItemQuantityDTO orderedItems;

    private List<ItemDetailDTO> itemsToPay;

    private Long paymentId;
}
