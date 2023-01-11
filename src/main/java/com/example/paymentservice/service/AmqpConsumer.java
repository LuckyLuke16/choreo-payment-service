package com.example.paymentservice.service;

import com.example.paymentservice.model.ItemDetailDTO;
import com.example.paymentservice.model.order.OrderDTO;
import com.example.paymentservice.model.paymentMethods.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmqpConsumer {

    private final PaymentService paymentService;

    private RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);

    public AmqpConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "#{fulfillPaymentQueue.name}")
    @SendTo("#{exchange.name}/payment.succeeded")
    public Message<OrderDTO> processOrder(Message<OrderDTO> orderDetailsMessage) {
        String userId = orderDetailsMessage.getHeaders().get("userId", String.class);
        String correlationId = orderDetailsMessage.getHeaders().get("correlationId", String.class);
        OrderDTO orderDetails = orderDetailsMessage.getPayload();
        String paymentMethod = orderDetails.getPaymentMethod();
        List<ItemDetailDTO> itemsToPay = orderDetails.getItemsToPay();

        try {
            Method method = Method.valueOf(paymentMethod);
            Long paymentId = this.paymentService.makePayment(userId, method, itemsToPay);
            orderDetails.setPaymentId(paymentId);

            return MessageBuilder
                    .withPayload(orderDetails)
                    .setHeader("userId", userId)
                    .setHeader("correlationId", correlationId)
                    .build();
        } catch (Exception e) {
            MessagePostProcessor messagePostProcessor = MessageProcessor.buildMessageProperties(correlationId);
            this.rabbitTemplate.convertAndSend("choreography-exchange", "payment.failed", orderDetails, messagePostProcessor);
            logger.warn("Payment could not be updated: correlation id: {}, user id: {}", correlationId, userId, e);
            throw new AmqpRejectAndDontRequeueException("Payment failed");
        }
    }
}
