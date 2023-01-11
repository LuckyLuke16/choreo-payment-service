package com.example.paymentservice.service;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

public class MessageProcessor {

    // user id is set as messageId
    public static MessagePostProcessor buildMessageProperties(String correlationId) {

        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties
                    = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId);
        return message;
        };

        return messagePostProcessor;
    }
}
