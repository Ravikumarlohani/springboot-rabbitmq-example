package com.ameritas.viafilehandler.rabbitmq.demo.consumer;

import com.ameritas.viafilehandler.rabbitmq.demo.config.MessagingConfig;
import com.ameritas.viafilehandler.rabbitmq.demo.dto.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(OrderStatus orderStatus) {
        System.out.println("Message recieved from queue : " + orderStatus);
    }
}
