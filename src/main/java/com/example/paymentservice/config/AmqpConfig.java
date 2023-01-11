package com.example.paymentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    static final String topicExchangeName = "choreography-exchange";

    static final String fulfillPaymentQueue = "fulfill-payment-queue";

    @Bean
    Queue fulfillPaymentQueue() {
        return new Queue(fulfillPaymentQueue, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding fulfillPaymentBinding(Queue fulfillPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(fulfillPaymentQueue).to(exchange).with("stock.updated");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
