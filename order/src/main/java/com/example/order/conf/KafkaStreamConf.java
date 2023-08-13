package com.example.order.conf;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Arrays;
import java.util.Locale;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConf {

    //定义流处理程序的拓扑结构 DLS高级processor API
    @Bean
    KStream mykStream1(StreamsBuilder streamsBuilder){
        //定义state store 用于保存计算中的结果
//        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore("Counts");
//        StoreBuilder storeBuilder= Stores.keyValueStoreBuilder(storeSupplier, Serdes.String(), new JsonSerde());
//        streamsBuilder.addStateStore(storeBuilder);
        //定义流处理器的拓扑结构
        KStream<String, String> kStream = streamsBuilder.stream("source-topic");
        kStream
                //过滤
                .filter((k,v) -> {
                    System.out.println(k + "初始状态"+ v);
                    return true;
                })
                //映射，生成新的流
                .map((k,v) -> {
                    return new KeyValue<String, String>(k,v+" hello");
                })
                //扁平处理，生成新的流，将每个value分割为对应的单词集合,并将所有集合的每个单词作为新记录进入新的流
                .flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String s) {
                        System.out.println("映射之后的状态"+s);
                        return Arrays.asList(s.toLowerCase(Locale.getDefault()).split("\\W+"));
                    }
                })
                //分组，生成分组流
                .groupBy(new KeyValueMapper<String, String, String>() {
                    @Override
                    public String apply(String s, String o) {
                        System.out.println(s + "扁平处理之后的状态"+ o);
                        return o;
                    }
                })
                //计数,并生成新的流，这里需要手动创建两个3-3的topic order-counts-store-repartition order-counts-store-changelog，否则2.4版本一下会报错，除非升级版本
                .count(Materialized.as("counts-store")).toStream()
                //映射，生成新的流
                .map((k,v) -> {
                    System.out.println(k + "分组并计数之后的状态"+ v);
                    return new KeyValue<String, String>(k,v+"");
                })
                //发送
                .to("sink-topic", Produced.with(Serdes.String(),Serdes.String()));//指定序列化器，并将结果输出到另外一个topic
        return kStream;
    }

}
