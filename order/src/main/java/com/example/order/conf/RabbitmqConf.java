package com.example.order.conf;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConf {

    public static final String exchange_name = "my_exchange";

    public static final String queue_name = "my_queue";

    public static final String tx_exchange_name = "tx_my_exchange";

    public static final String tx_queue_name = "tx_my_queue";

    public static final String dlx_exchange_name = "dlx_my_exchange";

    public static final String dlx_queue_name = "dlx_my_queue";

    /**
     * 交换机
     * @return
     */
    @Bean("tx_exchange")
    public Exchange txExchange(){
        return ExchangeBuilder.topicExchange(tx_exchange_name).durable(true).build();
    }

    /**
     * 队列
     * @return
     */
    @Bean("tx_queue")
    public Queue txQueue(){
        return QueueBuilder.durable(tx_queue_name)
//                .ttl(10000)//设置队列消息过期时间
                .deadLetterExchange(dlx_exchange_name)//绑定死信交换机
                .deadLetterRoutingKey("tx.#")//设置死信路由键
                .build();
    }

    /**
     * 绑定关系
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding txBinding(@Qualifier("tx_exchange") Exchange exchange, @Qualifier("tx_queue")Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("tx.#").noargs();
    }


    /**
     * 死信交换机
     * @return
     */
    @Bean("dlx_exchange")
    public Exchange dlxExchange(){
        return ExchangeBuilder.topicExchange(dlx_exchange_name).durable(true).build();
    }

    /**
     * 死信队列
     * @return
     */
    @Bean("dlx_queue")
    public Queue dlxQueue(){
        return QueueBuilder.durable(dlx_queue_name)
                .build();
    }

    /**
     * 绑定关系（死信）
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding dlxBinding(@Qualifier("dlx_exchange") Exchange exchange, @Qualifier("dlx_queue")Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
    }
}
