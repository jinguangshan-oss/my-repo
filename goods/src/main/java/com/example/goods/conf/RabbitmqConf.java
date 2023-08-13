package com.example.goods.conf;

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

    /**
     * 交换机
     * @return
     */
    @Bean("exchange")
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(exchange_name).durable(true).build();
    }

    /**
     * 队列
     * @return
     */
    @Bean("queue")
    public Queue queue(){
        return QueueBuilder.durable(queue_name).build();
    }

    /**
     * 绑定关系
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding binding(@Qualifier("exchange") Exchange exchange, @Qualifier("queue")Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("my.#").noargs();
    }


//    /**
//     * 交换机
//     * @return
//     */
//    @Bean("tx_exchange")
//    public Exchange txExchange(){
//        return ExchangeBuilder.topicExchange(tx_exchange_name).durable(true).build();
//    }
//
//    /**
//     * 队列
//     * @return
//     */
//    @Bean("tx_queue")
//    public Queue txQueue(){
//        return QueueBuilder.durable(tx_queue_name).build();
//    }
//
//    /**
//     * 绑定关系
//     * @param exchange
//     * @param queue
//     * @return
//     */
//    @Bean
//    public Binding txBinding(@Qualifier("tx_exchange") Exchange exchange, @Qualifier("tx_queue")Queue queue){
//        return BindingBuilder.bind(queue).to(exchange).with("tx.#").noargs();
//    }
}
