package com.sz.springbootsample.demo.service.rabbitmq;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import com.sz.springbootsample.demo.config.message.RabbitConfig;

/**
 * @author Yanghj
 * @date 4/17/2020
 */
@Component
public class RabbitReceiver {

    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE)
    public void consume(Message message, Channel channel) throws IOException {
        byte[] bytes = message.getBody();
        String demoVO = new String(bytes, "UTF-8");
        System.out.println(demoVO);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

    @RabbitListener(queues = RabbitConfig.MAIL_FANOUT_QUEUE)
    public void consume1(Message message, Channel channel) throws IOException {
        byte[] bytes = message.getBody();
        String demoVO = new String(bytes, "UTF-8");
        System.out.println(demoVO);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}
