package com.example.order.service;

import com.alibaba.fastjson.JSONObject;
import com.example.order.conf.RabbitmqConf;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * rabbitmq分布式事务解决方案
 */
@Component
public class MqService {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     *启动发布确认机制，设置rabbitTemplate回执方法
     */
    @PostConstruct
    public void seCallback(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                //b为true代表broker已经收到消息
                System.out.println("收到回执：CorrelationData="+correlationData + " ack=" + b);
                if(!b){
                    return;
                }
                //修改消息表状态
                try{
                    String sql = "update tb_distributed_message set msg_status=1 where unique_id=?";
                    jdbcTemplate.update(sql, correlationData.getId());
                }catch (Exception e){
                    System.out.println("修改本地消息表时发生异常！");
                }

            }
        });
    }

    /**
     * 发送消息
     * @param msg
     */
    public void sendMsg(JSONObject msg){
        //新增消息表记录
        try{
            String sql = "insert into tb_distributed_message (unique_id, msg_content, msg_status, create_time) values (?, ?, ?, now())";
            jdbcTemplate.update(sql, msg.get("orderId"), msg.toJSONString(), 0);
        }catch (Exception e){
            System.out.println("修改本地消息表时发生异常！");
            return;
        }
        rabbitTemplate.convertAndSend(RabbitmqConf.tx_exchange_name,"tx.11",msg.toJSONString(),new CorrelationData(msg.getString("orderId")));
    }

}
