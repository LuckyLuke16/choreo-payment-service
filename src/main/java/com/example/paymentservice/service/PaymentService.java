package com.example.paymentservice.service;

import com.example.paymentservice.exception.NoItemsFoundException;
import com.example.paymentservice.exception.PaymentFailedException;
import com.example.paymentservice.exception.PaymentSavingException;
import com.example.paymentservice.model.ItemDetailDTO;
import com.example.paymentservice.model.entity.Payment;
import com.example.paymentservice.model.paymentMethods.CreditCard;
import com.example.paymentservice.model.paymentMethods.Method;
import com.example.paymentservice.model.paymentMethods.PayPal;
import com.example.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private PayPal payPalInstance = new PayPal();

    private CreditCard creditCardInstance = new CreditCard();

    private final PaymentRepository paymentRepository;

    Logger logger = LoggerFactory.getLogger(PaymentService.class);


    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public Long makePayment(String userId, Method paymentMethod, List<ItemDetailDTO> itemsToPay) {
        try {
            float totalPrice = this.calculatePrice(itemsToPay);
            this.redirectToPaymentProvider(paymentMethod, totalPrice);
            return this.savePayment(userId, totalPrice, paymentMethod);
        } catch (Exception e) {
            throw new PaymentFailedException(paymentMethod);
        }
    }

    public long savePayment(String userId, float totalPrice, Method paymentMethod) {
        try {
            Payment paymentToSave = new Payment(userId, totalPrice, String.valueOf(paymentMethod));
            this.paymentRepository.save(paymentToSave);
            logger.info("Payment saved successfully");
            return paymentToSave.getPaymentId();
        } catch (Exception e) {
            throw new PaymentSavingException();
        }
    }

    private float calculatePrice(List<ItemDetailDTO> itemsToPay) {
        float totalPrice = 0;
        if(itemsToPay.isEmpty())
            throw new NoItemsFoundException();
        for(ItemDetailDTO item : itemsToPay) {
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

    private void redirectToPaymentProvider(Method paymentMethod, float totalPrice) {
        switch(paymentMethod) {
            case PAYPAL: this.payPalInstance.makeTransaction(totalPrice);
            case CREDIT_CARD: this.creditCardInstance.makeTransaction(totalPrice);
        }
        logger.info("Payment succeeded");
    }

}
