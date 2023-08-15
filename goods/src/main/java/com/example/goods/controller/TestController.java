package com.example.goods.controller;

import com.example.goods.conf.RabbitmqConf;
import com.example.goods.dao.GoodsMapper;
import com.example.goods.openfeignclient.Test;
import com.example.goods.type.Goods;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 该属性来自配置中心
     */
    @Value("${aaa}")
    String aaa;

    /**
     * openfeign声明式客户端
     */
    @Resource
    Test test;

    @Resource
    GoodsMapper goodsMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Resource
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/test")
    public String test(){
        //分布式锁
        RLock lock = redissonClient.getLock("mylock");
        lock.lock();
        rabbitTemplate.convertAndSend(RabbitmqConf.exchange_name,"my.11","hello");
        lock.unlock();
        //检查缓存
        List<Goods> testList = (List<Goods>) redisTemplate.opsForValue().get("test");
        if(testList != null){
            return "redis success";
        }


        //openfeign声明式远程调用
        String result = test.test();

        Goods goods = new Goods(null,"手链",null);
        List<Goods> goodsList = goodsMapper.select(goods);

        //加入缓存
        redisTemplate.opsForValue().set("test", goodsList);

        return aaa + result + UUID.randomUUID();
    }

    @RequestMapping("/test1")
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public String test1(){
        return "hello1";
    }

    @PostConstruct
    public void testJstack() throws InterruptedException {
        while (true){
            Thread.sleep(1000);
            System.out.printf("testJstack");
        }
    }

}
