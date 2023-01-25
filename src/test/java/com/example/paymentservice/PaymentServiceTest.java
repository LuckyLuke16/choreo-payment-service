package com.example.paymentservice;

import com.example.paymentservice.exception.PaymentFailedException;
import com.example.paymentservice.exception.PaymentSavingException;
import com.example.paymentservice.model.entity.Payment;
import com.example.paymentservice.model.paymentMethods.Method;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.PaymentService;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLTimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.anyOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;


    @BeforeEach
    void setup() {
        this.paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void Should_Save_Valid_Payment() {
        String userID = "1234";
        Float price = 9.95f;
        String paymentMethod = "PAYPAL";
        Payment paymentToSave = new Payment(userID,price,paymentMethod);
        when(this.paymentRepository.save(any())).thenReturn(paymentToSave);

        Long paymentId = this.paymentService.savePayment(userID, 9.95f, Method.PAYPAL);

        verify(this.paymentRepository,times(1)).save(paymentToSave);
        assertThat(paymentId).isEqualTo(0L);
    }

    @Test
    void Should_Save_2_Payments_With_IDs() {
        String userID = "1234";
        Float price = 9.95f;

        this.paymentService.savePayment(userID, price, Method.PAYPAL);
        this.paymentService.savePayment(userID, price, Method.PAYPAL);

        verify(this.paymentRepository,times(2)).save(any());
    }

    @Test
    void Should_Throw_PaymentSavingException_When_NO_DB_Connection() {
        String userID = "1234";
        Float price = 9.95f;
        String paymentMethod = "PAYPAL";
        Payment paymentToSave = new Payment(userID,price,paymentMethod);
        when(this.paymentRepository.save(any())).thenThrow(new JDBCConnectionException("error", new SQLTimeoutException()));

        assertThrows(PaymentSavingException.class, ()-> this.paymentService.savePayment(userID, 9.95f, Method.PAYPAL));

        verify(this.paymentRepository,times(1)).save(paymentToSave);
    }
}
