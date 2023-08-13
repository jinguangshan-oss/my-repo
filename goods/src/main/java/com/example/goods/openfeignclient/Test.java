package com.example.goods.openfeignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "order",path = "/test")
public interface Test {

    @RequestMapping("/test")
    String test();

}
