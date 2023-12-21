package com.sz.springbootsample.demo.config.message;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yanghj
 * @date 4/17/2020
 */
@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    public static final String MAIL_QUEUE = "mail.queue";
    public static final String MAIL_FANOUT_QUEUE = "mail.fanout.queue";
    public static final String MAIL_DIRECT_EXCHANGE = "mail.direct.exchange";
    public static final String MAIL_FANOUT_EXCHANGE = "mail.fanout.exchange";
    public static final String MAIL_ROUTING_KEY = "mail.routing.key";

    @Bean(MAIL_QUEUE)
    public Queue mailQueue() {
        return new Queue(MAIL_QUEUE, true);
    }

    @Bean(MAIL_FANOUT_QUEUE)
    public Queue mailFanoutQueue() {
        return new Queue(MAIL_FANOUT_QUEUE, true);
    }

    @Bean(MAIL_DIRECT_EXCHANGE)
    public DirectExchange mailExchange() {
        return new DirectExchange(MAIL_DIRECT_EXCHANGE, true, false);
    }

    @Bean(MAIL_FANOUT_EXCHANGE)
    public FanoutExchange mailFanoutExchange() {
        return new FanoutExchange(MAIL_FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Binding mailBinding(
            @Qualifier(MAIL_DIRECT_EXCHANGE) DirectExchange mailExchange,
            @Qualifier(MAIL_QUEUE) Queue mailQueue) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(MAIL_ROUTING_KEY);
    }

    @Bean
    public Binding mailFanoutBinding1(
            @Qualifier(MAIL_FANOUT_EXCHANGE) FanoutExchange mailExchange,
            @Qualifier(MAIL_QUEUE) Queue mailQueue) {
        return BindingBuilder.bind(mailQueue).to(mailExchange);
    }

    @Bean
    public Binding mailFanoutBinding2(
            @Qualifier(MAIL_FANOUT_EXCHANGE) FanoutExchange mailExchange,
            @Qualifier(MAIL_FANOUT_QUEUE) Queue mailQueue) {
        return BindingBuilder.bind(mailQueue).to(mailExchange);
    }
}
