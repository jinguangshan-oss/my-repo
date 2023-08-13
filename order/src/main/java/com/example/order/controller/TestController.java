package com.example.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.order.conf.RabbitmqConf;
import com.example.order.conf.ZkConf;
import com.example.order.service.MqService;
import com.example.order.type.Order;
import com.example.order.type.TbDistributedMessage;
import com.rabbitmq.client.Channel;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.zookeeper.CreateMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    MqService mqService;

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    CuratorFramework curatorFramework;

    @Resource
    KafkaTemplate kafkaTemplate;

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }

    @RabbitListener(queues = {"my_queue"})
    public void rabbitListner(String msg){
        System.out.println("收到消息:"+msg);
    }

    @RequestMapping("/testTx")
    public String testTx(){

        String sql = "select * from tb_distributed_message order by create_time desc limit 1";
        RowMapper<TbDistributedMessage> rowMapper = new BeanPropertyRowMapper<TbDistributedMessage>(TbDistributedMessage.class);
        List<TbDistributedMessage> messages = jdbcTemplate.query(sql,rowMapper);
        String uniqueId = messages.get(0).getUniqueId();
        uniqueId = String.valueOf(Integer.valueOf(uniqueId).intValue() + 1);

        Order order = new Order();
        order.setOrderId(uniqueId);
        order.setUserId("1");
        order.setAmount(new BigDecimal(1000));
        mqService.sendMsg((JSONObject) JSONObject.toJSON(order));
        return "ok";
    }

    @RabbitListener(queues = {RabbitmqConf.tx_queue_name})
    public void rabbitListnerTx(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception{
        System.out.println("收到消息:" + msg);
        try{

            //幂等处理（解决方案：全局唯一id，乐观锁,分布式锁）
            String key = JSONObject.parseObject(msg).getString("unique_id");
            boolean flag = redisTemplate.opsForValue().setIfAbsent(key,"value",1800000, TimeUnit.MILLISECONDS);
            if(!flag){
                //消息重复处理时,之间手动ack
                channel.basicAck(tag,false);
            }
            //执行业务操作

            //手动ack——消费成功
            channel.basicAck(tag,false);
        }catch (Exception e){
            //手动ack——消费不成功
            channel.basicNack(tag, false,false);
        }
    }

    /**
     * zk分布式锁
     * @return
     * @throws Exception
     */
    @RequestMapping("/zkLock")
    public String zkLock() throws Exception{
        //创建锁节点
        if (Objects.isNull(curatorFramework.checkExists().forPath(ZkConf.lockNodePath))) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ZkConf.lockNodePath);
        }
        //创建分布式锁
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, ZkConf.lockNodePath);
        //获取锁
        lock.acquire();
        //业务操作
        System.out.println("获取到锁");
        Thread.sleep(10000);
        System.out.println("开始释放锁");
        //释放锁
        lock.release();
        return "success";
    }

    @RequestMapping("/kafka")
    public String kafka() throws Exception{
        kafkaTemplate.send("source-topic","aaa");
        return "success";
    }

    @KafkaListener(topics = {"mytopic"})
    public String kafkaConsumer(ConsumerRecord<?,?> consumerRecord) throws Exception{
        System.out.println("收到kafka消息" + consumerRecord.value());
        return "success";
    }

    @KafkaListener(topics = {"sink-topic"})
    public String kafkaConsumer1(ConsumerRecord<?,?> consumerRecord) throws Exception{
        System.out.println("收到kafka消息" + consumerRecord.key() + consumerRecord.value());
        return "success";
    }

}
